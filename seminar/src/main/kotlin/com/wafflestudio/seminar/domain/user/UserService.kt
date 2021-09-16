package com.wafflestudio.seminar.domain.user

import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.global.common.exception.InvalidRequestException
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    val PARTICIPANT: String = "participant"
    val INSTRUCTOR: String = "instructor"

    fun signup(signupRequest: UserDto.SignupRequest): User {
        val encodedPassword = passwordEncoder.encode(signupRequest.password)

        // when 구문으로 대체 가능
        return if (signupRequest.role == PARTICIPANT) {
            val participantProfile = ParticipantProfile(signupRequest.university, signupRequest.accepted)
            userRepository.save(User(signupRequest.name, signupRequest.email, encodedPassword, PARTICIPANT, participantProfile, null))
        } else if (signupRequest.role == INSTRUCTOR) {
            val instructorProfile = InstructorProfile(signupRequest.company, signupRequest.year)
            userRepository.save(User(signupRequest.name, signupRequest.email, encodedPassword, INSTRUCTOR, null, instructorProfile))
        } else {
            throw Exception("role should be 'participant' or 'instructor'")
        }
    }

}