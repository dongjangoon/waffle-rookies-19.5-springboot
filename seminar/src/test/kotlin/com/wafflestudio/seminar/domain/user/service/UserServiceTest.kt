package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.exception.UserAlreadyExistsException
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.ParticipantProfileRepository
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.TestConstructor
import javax.transaction.Transactional

@AutoConfigureMockMvc
@SpringBootTest
internal class UserServiceTest {

    @MockBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var participantProfileRepository: ParticipantProfileRepository

    @MockBean
    private lateinit var passwordEncoder: PasswordEncoder

    @Autowired
    private lateinit var userService: UserService

    @BeforeEach
    internal fun setUp() {
        userService = UserService(userRepository, participantProfileRepository, passwordEncoder)
    }

    @Test
    @Transactional
    fun `회원가입에 성공한다`() {
        val user = signupAsInstructorUser("waffle")

        assertEquals(user.email, "waffle@snu.ac.kr")
        assertEquals(user.roles, "instructor")
    }

    @Test
    @Transactional
    fun `이미 가입된 메일이라면 예외가 발생한다`() {
        val user = signupAsInstructorUser("waffle")

        assertThrows<UserAlreadyExistsException> {
            signupAsInstructorUser("waffle")
        }

    }

    @Test
    fun update() {
    }

    @Test
    fun participateLater() {
    }

    private fun signupAsInstructorUser(name: String): User {
        val body = UserDto.SignupRequest(
            name,
            "${name}@snu.ac.kr",
            name,
            "instructor",
            company = "google",
            year = 1
        )
        return userService.signup(body)
    }
}