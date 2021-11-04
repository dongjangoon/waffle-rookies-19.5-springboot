package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.exception.AlreadyParticipatedException
import com.wafflestudio.seminar.domain.user.exception.UserAlreadyExistsException
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.ParticipantProfileRepository
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.TestConstructor
import javax.transaction.Transactional

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@AutoConfigureMockMvc
internal class UserServiceTest(
    private var userService: UserService,
    private var passwordEncoder: PasswordEncoder
) {
    @SpyBean
    private lateinit var userRepository: UserRepository

    @MockBean
    private lateinit var participantProfileRepository: ParticipantProfileRepository

    @BeforeEach
    fun setUp() {
        passwordEncoder = BCryptPasswordEncoder()
        userService = UserService(userRepository, participantProfileRepository, passwordEncoder)
    }

    @Test
    @Transactional
    fun `회원가입에 성공한다`() {
        val user = signupAsInstructorUser("json")

        val findUser = userRepository.findById(user.id).get()

        assertEquals(findUser.email, user.email)
        assertEquals(findUser.roles, user.roles)
        assertEquals(findUser.instructorProfile?.company, user.instructorProfile?.company)
    }

    @Test
    @Transactional
    fun `이미 가입된 메일이라면 예외가 발생한다`() {
        signupAsInstructorUser("waffle")

        assertThrows(UserAlreadyExistsException::class.java) {
            signupAsInstructorUser("waffle")
        }
    }

    @Test
    @Transactional
    fun `회원 정보 변경에 성공한다`() {
        val user = signupAsParticipantUser("brad")
        val request = UserDto.ModifyRequest(
            "Harvard",
            year = 3
        )

        userService.update(request, user)

        assertEquals(user.participantProfile?.university, "Harvard")
    }

    @Test
    @Transactional
    fun `나중에 참여자 등록에 성공한다`() {
        val user = signupAsInstructorUser("brad")
        val request = UserDto.ParticipantRequest(
            university = "서울대학교"
        )

        userService.participateLater(request, user)

        assertEquals(user.participantProfile?.university, "서울대학교")
    }

    @Test
    @Transactional
    fun `이미 참여자인 사람이 사후 참여자 등록할 경우에 실패한다`() {
        val participant = signupAsParticipantUser("brad")
        val request = UserDto.ParticipantRequest(
            university = "서울대학교"
        )

        assertThrows(AlreadyParticipatedException::class.java) {
            userService.participateLater(request, participant)
        }
    }

    private fun signupAsParticipantUser(name: String): User {
        val signupRequest = UserDto.SignupRequest(
            name = name,
            email = "${name}@gmail.com",
            password = name,
            role = "participant",
            university = "서울대학교"
        )
        return userService.signup(signupRequest)
    }

    private fun signupAsInstructorUser(name: String): User {
        val signupRequest = UserDto.SignupRequest(
            name = name,
            email = "${name}@gmail.com",
            password = name,
            role = "instructor",
            company = "google",
            year = 1
        )
        return userService.signup(signupRequest)
    }
}