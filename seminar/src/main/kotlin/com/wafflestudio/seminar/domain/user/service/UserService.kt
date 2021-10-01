package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.exception.AlreadyParticipatedException
import com.wafflestudio.seminar.domain.user.exception.InvalidRoleRequestException
import com.wafflestudio.seminar.domain.user.exception.UserAlreadyExistsException
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import com.wafflestudio.seminar.domain.user.model.Role
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.ParticipantProfileRepository
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    fun signup(signupRequest: UserDto.SignupRequest): User {
        if (userRepository.existsByEmail(signupRequest.email)) throw UserAlreadyExistsException()
        val encodedPassword = passwordEncoder.encode(signupRequest.password)

        return when (signupRequest.role) {
            Role.PARTICIPANT.role -> {
                val participantProfile = ParticipantProfile(
                    null, signupRequest.university, signupRequest.accepted, mutableListOf()
                )
                userRepository.save(User(
                    signupRequest.name, signupRequest.email, encodedPassword, Role.PARTICIPANT.role, participantProfile, null
                ))
            }
            Role.INSTRUCTOR.role -> {
                val instructorProfile = InstructorProfile(null, signupRequest.company, signupRequest.year)
                userRepository.save(User(signupRequest.name, signupRequest.email, encodedPassword, Role.INSTRUCTOR.role, null, instructorProfile))
            }
            else -> throw InvalidRoleRequestException("role should be 'participant' or 'instructor'")
        }
    }

    fun update(modifyRequest: UserDto.ModifyRequest, user: User): User {

        when (user.roles) {
            Role.PARTICIPANT.role -> {
                user.participantProfile?.university = modifyRequest.university }
            Role.INSTRUCTOR.role -> {
                user.instructorProfile?.company = modifyRequest.company
                user.instructorProfile?.year = modifyRequest.year
            }
            Role.BOTH.role -> {
                user.participantProfile?.university = modifyRequest.university
                user.instructorProfile?.company = modifyRequest.company
                user.instructorProfile?.year = modifyRequest.year
            }
        }

        return user
    }

    fun participateLater(participantRequest: UserDto.ParticipantRequest, user: User): User {
        if (user.roles != Role.INSTRUCTOR.role) throw AlreadyParticipatedException("You're already participant")

        user.roles = Role.BOTH.role

        val participantProfile = ParticipantProfile(user, participantRequest.university, participantRequest.accepted)
        participantProfileRepository.save(participantProfile)
        user.participantProfile = participantProfile

        return user
    }

}