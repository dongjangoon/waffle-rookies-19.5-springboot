package com.wafflestudio.seminar.domain.seminar.api

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.TestConstructor
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActionsDsl
import org.springframework.test.web.servlet.delete
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import javax.transaction.Transactional

@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class IntegrationTest(private val mockMvc: MockMvc) {

    private lateinit var authenticationParticipant: String
    private lateinit var authenticationInstructor: String
    private lateinit var authenticationInstructor2: String

    //  participant 1명, instructor 2명, 세미나 1개를 등록해놓습니다.
    @BeforeEach
    fun `회원가입`() {
        authenticationParticipant = signupAsParticipantUser("waffle").andReturn()
            .response.getHeader("Authentication")!!

        authenticationInstructor = signupAsInstructorUser("hodduk").andReturn()
            .response.getHeader("Authentication")!!
        authenticationInstructor2 = signupAsInstructorUser("snack").andReturn()
            .response.getHeader("Authentication")!!

        val body = """
                {
                    "name": "react",
                    "capacity": 40,
                    "count": 10,
                    "time": "12:00"
                }
                """.trimIndent()

        mockMvc.post("/api/v1/seminars/") {
            content = (body)
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
            header("Authentication", authenticationInstructor2)
        }
    }

    @Test
    @Transactional
    fun `세미나 등록에 성공한다`() {
        registerSeminarAsInstructor(
            """
                {
                    "name": "react",
                    "capacity": 40,
                    "count": 10,
                    "time": "12:00"
                }
            """.trimIndent()
        ).andExpect {
            status { isCreated() }
        }
    }

    @Test
    @Transactional
    fun `참여자가 세미나 등록을 시도할 경우 실패한다`() {
        registerSeminarAsParticipant(
            """
                {
                    "name": "ios",
                    "capacity": 40,
                    "count": 10,
                    "time": "12:00"
                }
            """.trimIndent()
        ).andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @Transactional
    fun `온라인 여부가 잘못된 입력일 경우 실패한다`() {
        registerSeminarAsInstructor(
            """
                {
                    "name": "spring",
                    "capacity": 40,
                    "count": 10,
                    "time": "12:00",
                    "online": "wrong_input"
                }
            """.trimIndent()
        ).andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    @Transactional
    fun `세미나 변경에 성공한다`() {
        updateSeminarAsCharger(
            """
                {
                    "capacity": 3,
                    "count": 20,
                    "online": "FalSe"
                }
            """.trimIndent()
        ).andExpect {
            status { isOk() }
        }
    }

    @Test
    @Transactional
    fun `세미나 담당자가 아닐 경우 변경에 실패한다`() {
        val body = """
                {
                    "capacity": 3,
                    "count": 20,
                    "online": "FalSe"
                }
            """.trimIndent()

        mockMvc.put("/api/v1/seminars/1/") {
            content = (body)
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
            header("Authentication", authenticationInstructor)
        }.andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @Transactional
    fun `capacity 가 참여 인원보다 작으면 실패한다`() {
        enterSeminarLaterAsParticipant(
            """{"role": "participant"}""".trimIndent()
        )

        updateSeminarAsCharger(
            """
                {
                    "capacity": 0,
                    "count": 20,
                    "online": "FalSe"
                }
            """.trimIndent()
        ).andExpect {
            status { isBadRequest() }
        }
    }

    @Test
    @Transactional
    fun `사후에 세미나 참여에 성공한다`() {
        enterSeminarLaterAsParticipant(
            """{"role": "participant"}""".trimIndent()
        ).andExpect {
            status { isCreated() }
        }
    }

    @Test
    @Transactional
    fun `사후에 세미나 담당자 등록에 성공한다`() {
        enterSeminarLaterAsInstructor(
            """{"role": "instructor"}""".trimIndent()
        ).andExpect {
            status { isCreated() }
        }
    }

    @Test
    @Transactional
    fun `진행자가 참여자로 요청할 세미나에 등록할 경우 실패한다`() {
        enterSeminarLaterAsInstructor(
            """{"role": "participant"}""".trimIndent()
        ).andExpect {
            status { isForbidden() }
        }
    }

    @Test
    @Transactional
    fun `세미나 중도 포기에 성공한다`() {
        enterSeminarLaterAsParticipant(
            """{"role": "participant"}""".trimIndent()
        )

        mockMvc.delete("/api/v1/seminars/1/user/me/") {
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
            header("Authentication", authenticationParticipant)
        }.andExpect {
            status { isOk() }
        }
    }

    @Test
    @Transactional
    fun `진행자가 세미나 중도 포기할 경우 실패한다`() {
        mockMvc.delete("/api/v1/seminars/1/user/me/") {
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
            header("Authentication", authenticationInstructor2)
        }.andExpect {
            status { isForbidden() }
        }
    }

    private fun enterSeminarLaterAsParticipant(body: String): ResultActionsDsl {
        return mockMvc.post("/api/v1/seminars/1/user/") {
            content = (body)
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
            header("Authentication", authenticationParticipant)
        }
    }

    private fun enterSeminarLaterAsInstructor(body: String): ResultActionsDsl {
        return mockMvc.post("/api/v1/seminars/1/user/") {
            content = (body)
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
            header("Authentication", authenticationInstructor)
        }
    }

    private fun updateSeminarAsCharger(body: String): ResultActionsDsl {
        return mockMvc.put("/api/v1/seminars/1/") {
            content = (body)
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
            header("Authentication", authenticationInstructor2)
        }
    }

    private fun registerSeminarAsInstructor(body: String): ResultActionsDsl {
        return mockMvc.post("/api/v1/seminars/") {
            content = (body)
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
            header("Authentication", authenticationInstructor)
        }
    }

    private fun registerSeminarAsParticipant(body: String): ResultActionsDsl {
        return mockMvc.post("/api/v1/seminars/") {
            content = (body)
            contentType = (MediaType.APPLICATION_JSON)
            accept = (MediaType.APPLICATION_JSON)
            header("Authentication", authenticationParticipant)
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

}