package com.wafflestudio.seminar.domain.seminar.service

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.seminar.exception.AlreadyChargeException
import com.wafflestudio.seminar.domain.seminar.exception.AlreadyFullException
import com.wafflestudio.seminar.domain.seminar.exception.InstructorGiveUpException
import com.wafflestudio.seminar.domain.seminar.exception.InvalidCapacityRequestException
import com.wafflestudio.seminar.domain.seminar.exception.InvalidOnlineRequestException
import com.wafflestudio.seminar.domain.seminar.exception.NotAcceptedException
import com.wafflestudio.seminar.domain.seminar.exception.NotChargeException
import com.wafflestudio.seminar.domain.seminar.exception.NotInstructorException
import com.wafflestudio.seminar.domain.seminar.exception.NotRoleSuitableException
import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.data.repository.findByIdOrNull
import org.springframework.test.context.TestConstructor
import javax.transaction.Transactional

@AutoConfigureMockMvc
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@SpringBootTest
internal class SeminarServiceTest(
    private var seminarService: SeminarService
) {
    @SpyBean(name = "seminarRepository")
    private lateinit var seminarRepository: SeminarRepository

    @SpyBean(name = "userRepository")
    private lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        seminarService = SeminarService(seminarRepository, userRepository)
    }

    @Test
    @Transactional
    fun `????????? ????????? ????????????`() {
        val user = signupAsInstructor("waffle")
        val request = SeminarDto.RegisterRequest("ios", 40, 10, "12:00", "true")

        val seminar = seminarService.register(request, user)

        val findSeminar = seminarRepository.findByIdOrNull(seminar.id)
        assertEquals(findSeminar?.name, seminar.name)
        assertEquals(findSeminar?.capacity, seminar.capacity)
        assertEquals(findSeminar?.count, seminar.count)
    }

    @Test
    @Transactional
    fun `???????????? ????????? ????????? ????????? ????????? ????????????`() {
        val user = signupAsParticipant("waffle")
        val request = SeminarDto.RegisterRequest("ios", 40, 10, "12:00", "true")

        assertThrows(NotInstructorException::class.java) {
            seminarService.register(request, user)
        }
    }

    @Test
    @Transactional
    fun `body ??? ????????? ????????? ?????? ????????? ????????? ?????? ????????????`() {
        val user = signupAsInstructor("waffle")
        val request = SeminarDto.RegisterRequest("ios", 40, 10, "12:00", "wrong_input")

        assertThrows(InvalidOnlineRequestException::class.java) {
            seminarService.register(request, user)
        }
    }

    @Test
    @Transactional
    fun `????????? ????????? ????????????`() {
        val user = signupAsInstructor("waffle")
        val seminar = register(user)
        val request = SeminarDto.UpdateRequest(30, 3, "10:00", null)

        val updatedSeminar = seminarService.update(seminar.id, request, user)

        val findSeminar = seminarRepository.findByIdOrNull(updatedSeminar.id)
        assertEquals(findSeminar?.capacity, updatedSeminar.capacity)
        assertEquals(findSeminar?.count, updatedSeminar.count)
        assertEquals(findSeminar?.time, updatedSeminar.time)
        assertEquals(findSeminar?.online, updatedSeminar.online)
    }

    @Test
    @Transactional
    fun `???????????? ?????? ???????????? ????????? ????????? ????????? ?????? ????????????`() {
        val charger = signupAsInstructor("waffle")
        val notCharger = signupAsInstructor("hodduk")
        val seminar = register(charger)
        val request = SeminarDto.UpdateRequest(30, 3, "10:00", null)

        assertThrows(NotChargeException::class.java) {
            seminarService.update(seminar.id, request, notCharger)
        }
    }

    @Test
    @Transactional
    fun `capacity ??? ??????????????? ????????? ????????????`() {
        val instructor = signupAsInstructor("waffle")
        val participant = signupAsParticipant("hodduk")
        val seminar = register(instructor)
        val enterRequest = SeminarDto.EnterRequest("participant")
        val updateRequest = SeminarDto.UpdateRequest(0, 3, "10:00", null)

        seminarService.enterSeminar(seminar.id, enterRequest, participant)

        assertThrows(InvalidCapacityRequestException::class.java) {
            seminarService.update(seminar.id, updateRequest, instructor)
        }
    }

    @Test
    @Transactional
    fun `?????? ???????????? ???????????? ???????????? ???????????? ??? ????????????`() {
        val request = SeminarDto.RegisterRequest("ios3", 40, 10, "12:00", "true")
        val user = signupAsInstructor("waffle")
        val user2 = signupAsInstructor("bread")
        val map = mapOf("name" to "ios")
        val seminar1 = register(user)
        val seminar2 = seminarService.register(request, user2)

        val seminars = seminarService.getSeminarsByQueryParams(map)

        assertEquals(seminars.size, 2)
        assertEquals(seminars[0], seminar2)
        assertEquals(seminars[1], seminar1)
    }

    @Test
    @Transactional
    fun `????????? ????????? ????????? ?????? ???????????? ???????????? ???????????? ???????????? ??? ????????????`() {
        val request = SeminarDto.RegisterRequest("ios3", 40, 10, "12:00", "true")
        val user = signupAsInstructor("waffle")
        val user2 = signupAsInstructor("bread")
        val map = mapOf("name" to "ios", "order" to "earliest")
        val seminar1 = register(user)
        val seminar2 = seminarService.register(request, user2)

        val seminars = seminarService.getSeminarsByQueryParams(map)

        assertEquals(seminars.size, 2)
        assertEquals(seminars[0], seminar1)
        assertEquals(seminars[1], seminar2)
    }

    @Test
    @Transactional
    fun `????????? ???????????? ???????????? ??? ????????????`() {
        val instructor = signupAsInstructor("waffle")
        val participant = signupAsParticipant("hodduk")
        val seminar = register(instructor)
        val enterRequest = SeminarDto.EnterRequest("participant")

        seminarService.enterSeminar(seminar.id, enterRequest, participant)

        assertEquals(seminar.seminarParticipants.size, 1)
    }

    @Test
    @Transactional
    fun `?????? ?????? role ??? ???????????? ??? ?????? ????????????`() {
        val instructor = signupAsInstructor("waffle")
        val participant = signupAsParticipant("hodduk")
        val seminar = register(instructor)
        val enterRequest = SeminarDto.EnterRequest("instructor")

        assertThrows(NotRoleSuitableException::class.java) {
            seminarService.enterSeminar(seminar.id, enterRequest, participant)
        }
    }

    @Test
    @Transactional
    fun `???????????? ?????? ?????? ??? ?????? ????????????`() {
        val instructor = signupAsInstructor("waffle")
        val participant = signupAsParticipant("hodduk")
        val participant2 = signupAsParticipant("bread")
        val registerRequest = SeminarDto.RegisterRequest("ios", 1, 10, "12:00", "true")
        val seminar = seminarService.register(registerRequest, instructor)
        val enterRequest = SeminarDto.EnterRequest("participant")
        seminarService.enterSeminar(seminar.id, enterRequest, participant)

        assertThrows(AlreadyFullException::class.java) {
            seminarService.enterSeminar(seminar.id, enterRequest, participant2)
        }
    }

    @Test
    @Transactional
    fun `accepted ??? false ??? ?????? ????????????`() {
        val instructor = signupAsInstructor("waffle")
        val seminar = register(instructor)
        val request = SeminarDto.EnterRequest("participant")
        val participantProfile = ParticipantProfile(null, "snu", false)
        val participant = User("name", "name@gmail.com", "name", "participant", participantProfile, null)
        userRepository.save(participant)

        assertThrows(NotAcceptedException::class.java) {
            seminarService.enterSeminar(seminar.id, request, participant)
        }
    }

    @Test
    @Transactional
    fun `?????? ???????????? ?????? ???????????? ?????? ?????? ????????????`() {
        val instructor = signupAsInstructor("waffle")
        val instructor2 = signupAsInstructor("bread")
        val seminar = register(instructor)
        val seminar2 = register(instructor2)
        val request = SeminarDto.EnterRequest("instructor")

        assertThrows(AlreadyChargeException::class.java) {
            seminarService.enterSeminar(seminar2.id, request, instructor)
        }
    }

    @Test
    @Transactional
    fun `????????? ?????? ????????? ????????????`() {
        val instructor = signupAsInstructor("waffle")
        val participant = signupAsParticipant("hodduk")
        val seminar = register(instructor)
        val request = SeminarDto.EnterRequest("participant")
        seminarService.enterSeminar(seminar.id, request, participant)

        seminarService.dropSeminar(seminar.id, participant)

        assertEquals(seminar.seminarParticipants.find {
            it.participantProfile.id == participant.participantProfile?.id
        }?.isActive, false)
    }

    @Test
    @Transactional
    fun `???????????? ?????? ????????? ?????? ????????????`() {
        val instructor = signupAsInstructor("waffle")
        val seminar = register(instructor)

        assertThrows(InstructorGiveUpException::class.java) {
            seminarService.dropSeminar(seminar.id, instructor)
        }
    }

    private fun register(user: User): Seminar {
        val request = SeminarDto.RegisterRequest("ios", 40, 10, "12:00", "true")
        return seminarService.register(request, user)
    }

    private fun signupAsInstructor(name: String): User {
        val instructorProfile = InstructorProfile(null, "waffleStudio", 3)
        val user = User(name, "${name}@gmail.com", name, "instructor", null, instructorProfile)

        return userRepository.save(user)
    }

    private fun signupAsParticipant(name: String): User {
        val participantProfile = ParticipantProfile(null, "snu")
        val user = User(name, "${name}@gmail.com", name, "participant", participantProfile, null)

        return userRepository.save(user)
    }
}