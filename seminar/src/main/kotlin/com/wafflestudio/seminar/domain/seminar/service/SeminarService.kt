package com.wafflestudio.seminar.domain.seminar.service

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.seminar.exception.*
import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import com.wafflestudio.seminar.domain.seminar.repository.SeminarRepository
import com.wafflestudio.seminar.domain.user.model.Role
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import javax.transaction.Transactional

@Service
@Transactional
class SeminarService(
    private val seminarRepository: SeminarRepository,
    private val userRepository: UserRepository,
) {
    fun register(registerRequest: SeminarDto.RegisterRequest, user: User): Seminar {
        if (!user.roles.contains(Role.INSTRUCTOR.role)) throw NotInstructorException("NOT_INSTRUCTOR")

        val seminar = Seminar(registerRequest.name, registerRequest.capacity, registerRequest.count, registerRequest.time)
        when (registerRequest.online?.toLowerCase()) {
            "true" -> seminar.online = true
            "false" -> seminar.online = false
            null -> seminar.online = true
            else -> throw InvalidOnlineRequestException("Online should be 'true' or 'false'")
        }

        val findUser = userRepository.findByIdOrNull(user.id)
        seminar.instructorProfile.add(findUser!!.instructorProfile!!)
        findUser.instructorProfile?.seminar = seminar

        return seminarRepository.save(seminar)
    }

    fun update(seminarId: Long, updateRequest: SeminarDto.UpdateRequest, user: User): Seminar {
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: throw SeminarNotFoundException("SEMINAR NOT FOUND")

        if (!seminar.instructorProfile.any { it ->
            it.id == user.instructorProfile?.id
        }) throw NotChargeException("You are not charger")

        updateRequest.online?.let { it ->
            when (it.toLowerCase()) {
                "true" -> seminar.online = true
                "false" -> seminar.online = false
                else -> throw InvalidOnlineRequestException("Online should be 'true' or 'false'")
            }
        }

        updateRequest.capacity?.let { it ->
            if (it < seminar.seminarParticipants.count { it.isActive })
                throw InvalidCapacityRequestException("CAPACITY TOO SMALL")
        }

        seminar.apply {
            updateRequest.capacity?.let { capacity = it }
            updateRequest.count?.let { count = it }
            updateRequest.time?.let { time = it }
        }

        return seminar
    }

    fun getSeminarsByQueryParams(allParams: Map<String, String>): List<Seminar> {

        return when {
            allParams.keys.containsAll(listOf("name", "order")) -> {
                if (allParams["order"] == "earliest") seminarRepository.findByNameContainingOrderByCreatedAtAsc(allParams["name"]!!)
                else seminarRepository.findByNameContainingOrderByCreatedAtDesc(allParams["name"]!!)
            }
            allParams.keys.contains("name") -> {
                seminarRepository.findByNameContainingOrderByCreatedAtDesc(allParams["name"]!!)
            }
            allParams.keys.contains("order") -> {
                if (allParams["order"] == "earliest") seminarRepository.findAllByOrderByCreatedAtAsc()
                else seminarRepository.findAllByOrderByCreatedAtDesc()
            }
            else -> {
                seminarRepository.findAllByOrderByCreatedAtDesc()
            }
        }

    }

    fun enterSeminar(seminarId: Long, enterRequest: SeminarDto.EnterRequest, user: User): Seminar {
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: throw SeminarNotFoundException("SEMINAR NOT FOUND")

        val requestRole = enterRequest.role
        if (!user.roles.contains(requestRole)) throw NotRoleSuitableException("ROLE NOT SUITABLE")

        if (seminar.capacity <= seminar.seminarParticipants.size) throw AlreadyFullException("SEMINAR ALREADY FULL")

        if (seminar.seminarParticipants.any { it.participantProfile.user?.id == user.id }
            || seminar.instructorProfile.any { it.user?.id == user.id }) {
            throw AlreadyEnteredException("ALREADY IN SEMINAR or DROPPED SEMINAR")
        }

        when(requestRole) {
            Role.PARTICIPANT.role -> {
                user.participantProfile ?: throw NotRoleSuitableException("ROLE NOT SUITABLE")
                if(!user.participantProfile!!.accepted) throw NotAcceptedException("CANNOT PARTICIPATE")

                val seminarParticipant = SeminarParticipant(seminar, user.participantProfile!!)
                seminar.seminarParticipants.add(seminarParticipant)
                user.participantProfile!!.seminars.add(seminarParticipant)
            }
            Role.INSTRUCTOR.role -> {
                user.instructorProfile ?: throw NotRoleSuitableException("ROLE NOT SUITABLE")
                if(user.instructorProfile?.seminar != null) throw AlreadyChargeException("You're charged")

                user.instructorProfile!!.seminar = seminar
                seminar.instructorProfile.add(user.instructorProfile!!)
            }
        }

        return seminar
    }

    fun dropSeminar(seminarId: Long, user: User): Seminar {
        val seminar = seminarRepository.findByIdOrNull(seminarId) ?: throw SeminarNotFoundException("SEMINAR NOT FOUND")
        if (seminar.instructorProfile.any { it.id == user.instructorProfile?.id }) throw InstructorGiveUpException("INSTRUCTOR CANNOT DROP")

        seminar.seminarParticipants.find {
            it.participantProfile.id == user.participantProfile?.id
        }?.apply {
            isActive = false
            droppedAt = LocalDateTime.now()
        } ?: return seminar

        return seminar
    }

}