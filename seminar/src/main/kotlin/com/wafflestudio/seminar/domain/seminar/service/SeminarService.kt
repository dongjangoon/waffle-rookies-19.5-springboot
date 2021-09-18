package com.wafflestudio.seminar.domain.seminar.service

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.seminar.exception.InvalidOnlineRequestException
import com.wafflestudio.seminar.domain.seminar.exception.InvalidTimeRequestException
import com.wafflestudio.seminar.domain.seminar.exception.NotInstructorException
import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.domain.user.exception.UserNotFoundException
import com.wafflestudio.seminar.domain.user.model.Role
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class SeminarService(
    private val seminarRepository: SeminarRepository,
    private val userRepository: UserRepository,
) {
    fun register(registerRequest: SeminarDto.RegisterRequest, user: User): Seminar {
        if (user.roles != Role.INSTRUCTOR.role) throw NotInstructorException()

        val regex = "^([1-9]|[01][0-9]|2[0-3]):([0-5][0-9])$".toRegex()
        if (!registerRequest.time.matches(regex)) throw InvalidTimeRequestException("WRONG TIME FORMAT")

        val seminar =
            Seminar(registerRequest.name, registerRequest.capacity, registerRequest.count, registerRequest.time)
        when (registerRequest.online?.toLowerCase()) {
            "true" -> seminar.online = true
            "false" -> seminar.online = false
        }

        // Dirty Checking 을 위해 User 다시 조회
        val findUser = userRepository.findByIdOrNull(user.id) ?: throw UserNotFoundException()
        findUser.instructorProfile?.seminar = seminar
        seminar.instructorProfile.add(findUser.instructorProfile!!)

        return seminarRepository.save(seminar)
    }

}