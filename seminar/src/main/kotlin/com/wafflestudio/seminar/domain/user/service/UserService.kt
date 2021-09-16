package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.exception.RoleBadRequestException
import com.wafflestudio.seminar.domain.user.exception.UserAlreadyExistsException
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    val PARTICIPANT: String = "participant"
    val INSTRUCTOR: String = "instructor"

    fun signup(signupRequest: UserDto.SignupRequest): User {
        if (userRepository.existsByEmail(signupRequest.email)) throw UserAlreadyExistsException()
        val encodedPassword = passwordEncoder.encode(signupRequest.password)

        // when 구문으로 대체 가능
        return if (signupRequest.role == PARTICIPANT) {
            val participantProfile = ParticipantProfile(signupRequest.university, signupRequest.accepted)
            userRepository.save(User(signupRequest.name, signupRequest.email, encodedPassword, PARTICIPANT, participantProfile, null))
        } else if (signupRequest.role == INSTRUCTOR) {
            val instructorProfile = InstructorProfile(signupRequest.company, signupRequest.year)
            userRepository.save(User(signupRequest.name, signupRequest.email, encodedPassword, INSTRUCTOR, null, instructorProfile))
        } else {
            throw RoleBadRequestException("role should be 'participant' or 'instructor'")
        }
    }

    fun findUserByEmail(email: String?): User? {
        return userRepository.findByEmail(email)
    }

}