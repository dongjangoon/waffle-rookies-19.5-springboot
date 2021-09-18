package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.exception.AlreadyParticipatedException
import com.wafflestudio.seminar.domain.user.exception.RoleBadRequestException
import com.wafflestudio.seminar.domain.user.exception.UserAlreadyExistsException
import com.wafflestudio.seminar.domain.user.exception.UserNotFoundException
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.InstructorProfileRepository
import com.wafflestudio.seminar.domain.user.repository.ParticipantProfileRepository
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import javax.transaction.Transactional

@Service
@Transactional
class UserService(
    private val userRepository: UserRepository,
    private val participantProfileRepository: ParticipantProfileRepository,
    private val instructorProfileRepository: InstructorProfileRepository,
    private val passwordEncoder: PasswordEncoder,
) {
    val PARTICIPANT: String = "participant"
    val INSTRUCTOR: String = "instructor"
    val BOTH: String = "participant, instructor"

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

    @Transactional
    fun update(modifyRequest: UserDto.ModifyRequest, user: User): User {
        val findUser = userRepository.findByIdOrNull(user.id) ?: throw UserNotFoundException()
        when (findUser.roles) {
            PARTICIPANT -> {
                findUser.participantProfile?.university = modifyRequest.university }
            INSTRUCTOR -> {
                findUser.instructorProfile?.company = modifyRequest.company
                findUser.instructorProfile?.year = modifyRequest.year
            }
            BOTH -> {
                findUser.participantProfile?.university = modifyRequest.university
                findUser.instructorProfile?.company = modifyRequest.company
                findUser.instructorProfile?.year = modifyRequest.year
            }
        }
        return findUser
    }

    // Think: 유저 조회를 안하고 좀 더 간단하게 영속성 flush 할 방법이 없을까
    @Transactional
    fun participateLater(participantRequest: UserDto.ParticipantRequest, user: User): User {
        if (user.roles != INSTRUCTOR) throw AlreadyParticipatedException("You're already participant")

        val findUser = userRepository.findByIdOrNull(user.id) ?: throw UserNotFoundException()
        findUser.roles = "participant, instructor"
        val participantProfile = ParticipantProfile(participantRequest.university, participantRequest.accepted)
        participantProfileRepository.save(participantProfile)
        findUser.participantProfile = participantProfile
        return findUser
    }

}