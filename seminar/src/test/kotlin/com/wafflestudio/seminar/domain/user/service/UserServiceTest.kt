package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.servlet.ResultActionsDsl
import javax.transaction.Transactional

@AutoConfigureMockMvc
@SpringBootTest
internal class UserServiceTest {

    @MockBean
    lateinit var repository: UserRepository

    @MockBean
    lateinit var service: UserService

    @Test
    @Transactional
    fun `회원가입 성공`() {

    }

    @Test
    @Transactional
    fun `회원가입 중복 이메일 검증`() {

    }

    @Test
    fun update() {
    }

    @Test
    fun participateLater() {
    }

    private fun signupAsInstructorUser(name: String): User {
        val body =
            """
                {
                    "name": "$name",
                    "email": "${name}@snu.ac.kr",
                    "password": "$name",
                    "role": "instructor",
                    "company": "google",
                    "year": "1",
                }
            """.trimIndent()
        return signup(body)
    }

    private fun signup(body: UserDto.SignupRequest): User {
        return service.signup(body)
    }
}