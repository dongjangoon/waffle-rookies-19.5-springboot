package com.wafflestudio.seminar.domain.user.api

import org.hamcrest.Matchers
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import javax.transaction.Transactional

@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class IntegrationTest(private val mockMvc: MockMvc) {

    // Authentication
    private lateinit var authenticationParticipant: String
    private lateinit var authenticationInstructor: String

    @BeforeEach
    fun `회원가입`() {
        val participant = signupAsParticipantUser("waffle").andExpect {
            status { isCreated() }
            header { exists("Authentication") }
        }
        authenticationParticipant = participant.andReturn()
            .response.getHeader("Authentication")!!

        val instructor = signupAsInstructorUser("hodduk").andExpect {
            status { isCreated() }
            header { exists("Authentication") }
        }
        authenticationInstructor = instructor.andReturn()
            .response.getHeader("Authentication")!!
    }

    @Test
    @Transactional
    fun `회원 가입 정상 동작 검증`() {
        signupAsParticipantUser("waffle2").andExpect {
            status { isCreated() }
            header { exists("Authentication") }
        }
    }

    @Test
    @Transactional
    fun `중복 이메일 가입 불가능 조건 검증`() {
        signupAsParticipantUser("waffle").andExpect {
            status { isConflict() }
        }
    }

    @Test
    @Transactional
    fun `회원 가입 요청 body 오류`() {
        signup(
            """
                {
                    "name": "wrong_role",
                    "password": "password",
                    "email": "wrong@snu.ac.kr",
                    "role": "wrong_role",
                    "university": "서울대학교"
                }
            """.trimIndent()
        ).andExpect { status { isBadRequest() } }
        signup(
            """
                {
                    "name": "no_role",
                    "password": "password",
                    "email": "no_role@snu.ac.kr",
                    "university": "서울대학교"
                }
            """.trimIndent()
        ).andExpect { status { isBadRequest() } }
    }

    @Test
    @Transactional
    fun `아이디로 회원 조회를 성공한다`() {
        val uri = "/api/v1/users/1/"

        mockMvc.perform(MockMvcRequestBuilders.get(uri).header("Authentication", authenticationParticipant))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Transactional
    fun `존재하지 않는 회원 조회에 실패한다`() {
        val uri = "/api/v1/users/3/"

        mockMvc.perform(MockMvcRequestBuilders.get(uri).header("Authentication", authenticationParticipant))
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    @Transactional
    fun `현재 로그인된 유저 조회에 성공한다`() {
        val uri = "/api/v1/users/me/"

        mockMvc.perform(MockMvcRequestBuilders.get(uri).header("Authentication", authenticationParticipant))
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    @Transactional
    fun `현재 로그인된 유저 정보 변경에 성공한다`() {
        update(
            """
                {
                    "university": "jeju_university"
                }
            """.trimIndent()
        ).andExpect {
            status { isOk() }
            content { string(
                Matchers.containsString(
                    """
                        "university":"jeju_university"
                    """.trimIndent()
                ))
            }
        }
    }

    @Test
    @Transactional
    fun `사후 참여자 등록에 성공한다`() {
        participateLaterByInstructor(
            """
                {
                    "university": "waffleUniversity",
                    "accepted": "true"
                }
            """.trimIndent()
        ).andExpect {
            status{ isCreated() }
            content { string(
                Matchers.containsString(
                    """"participantProfile":{"id":2,"university":"waffleUniversity","accepted":true,"seminars":[]""".trimIndent()
                ))
            }
        }
    }

    @Test
    @Transactional
    fun `이미 참여자로 등록되어 있는 경우 사후 참여자 등록에 실패한다`() {
        participateLaterByParticipant(
            """
                {
                    "university": "waffleUniversity",
                    "accepted": "true"
                }
            """.trimIndent()
        ).andExpect {
            status { isConflict() }
        }
    }


    private fun signupAsInstructorUser(name: String): ResultActionsDsl {
        val body =
            """
                {
                    "password": "$name",
                    "name": "$name",
                    "email": "${name}@snu.ac.kr",
                    "role": "instructor",
                    "company": "wafflestudio",
                    "year": "1"
                }
            """.trimIndent()
        return signup(body)
    }

    private fun signupAsParticipantUser(name: String): ResultActionsDsl {
        val body =
            """
                {
                    "password": "$name",
                    "name": "$name",
                    "email": "${name}@snu.ac.kr",
                    "role": "participant"
                }
            """.trimIndent()
        return signup(body)
    }

    private fun signup(body: String): ResultActionsDsl {
        return mockMvc.post("/api/v1/users/") {
            content = (body)
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
        }
    }

    private fun update(body: String): ResultActionsDsl {
        return mockMvc.put("/api/v1/users/me/") {
            content = (body)
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
            header("Authentication", authenticationParticipant)
        }
    }

    private fun participateLaterByInstructor(body: String): ResultActionsDsl {
        return mockMvc.post("/api/v1/users/participant/") {
            content = (body)
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
            header("Authentication", authenticationInstructor)
        }
    }

    private fun participateLaterByParticipant(body: String): ResultActionsDsl {
        return mockMvc.post("/api/v1/users/participant/") {
            content = (body)
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
            header("Authentication", authenticationParticipant)
        }
    }
}