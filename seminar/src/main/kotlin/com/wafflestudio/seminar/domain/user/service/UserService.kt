package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.exception.AlreadyParticipatedException
import com.wafflestudio.seminar.domain.user.exception.InvalidRoleRequestException
import com.wafflestudio.seminar.domain.user.exception.UserAlreadyExistsException
import com.wafflestudio.seminar.domain.user.exception.UserNotFoundException
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import com.wafflestudio.seminar.domain.user.model.Role
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.ParticipantProfileRepository
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import com.wafflestudio.seminar.global.auth.dto.LoginRequest
import org.springframework.data.repository.findByIdOrNull
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
                val participantProfile = ParticipantProfile(null, signupRequest.university, signupRequest.accepted, mutableListOf())
                userRepository.save(User(signupRequest.name, signupRequest.email, encodedPassword, Role.PARTICIPANT.role, participantProfile, null))
            }
            Role.INSTRUCTOR.role -> {
                val instructorProfile = InstructorProfile(null, signupRequest.company, signupRequest.year)
                userRepository.save(User(signupRequest.name, signupRequest.email, encodedPassword, Role.INSTRUCTOR.role, null, instructorProfile))
            }
            else -> throw InvalidRoleRequestException("role should be 'participant' or 'instructor'")
        }
    }

    fun update(modifyRequest: UserDto.ModifyRequest, user: User): User {
        val findUser = userRepository.findById(user.id).orElseThrow() ?: throw UserNotFoundException()
        when (findUser.roles) {
            Role.PARTICIPANT.role -> {
                findUser.participantProfile?.university = modifyRequest.university }
            Role.INSTRUCTOR.role -> {
                findUser.instructorProfile?.company = modifyRequest.company
                findUser.instructorProfile?.year = modifyRequest.year
            }
            Role.BOTH.role -> {
                findUser.participantProfile?.university = modifyRequest.university
                findUser.instructorProfile?.company = modifyRequest.company
                findUser.instructorProfile?.year = modifyRequest.year
            }
        }
        return findUser
    }

    // Think: 유저 조회를 안하고 좀 더 간단하게 영속성 flush 할 방법이 없을까
    fun participateLater(participantRequest: UserDto.ParticipantRequest, user: User): User {
        if (user.roles != Role.INSTRUCTOR.role) throw AlreadyParticipatedException("You're already participant")

        val findUser = userRepository.findById(user.id).orElseThrow() ?: throw UserNotFoundException()
        findUser.roles = Role.BOTH.role

        val participantProfile = ParticipantProfile(findUser, participantRequest.university, participantRequest.accepted)
        participantProfileRepository.save(participantProfile)
        findUser.participantProfile = participantProfile

        return findUser
    }

}