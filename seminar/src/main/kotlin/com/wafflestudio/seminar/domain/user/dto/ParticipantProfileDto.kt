package com.wafflestudio.seminar.domain.user.dto

import com.wafflestudio.seminar.domain.seminar.dto.SeminarParticipantDto
import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import java.time.LocalDateTime

class ParticipantProfileDto {
    data class SeminarResponse(
        val id: Long?,
        val name: String?,
        val email: String?,
        val university: String?,
        val joinedAt: LocalDateTime?,
        val isActive: Boolean,
        val droppedAt: LocalDateTime?,
    ) {
        constructor(participantProfile: ParticipantProfile, seminarParticipant: SeminarParticipant): this(
            id = participantProfile.user?.id,
            name =participantProfile.user?.name,
            email = participantProfile.user?.email,
            university = participantProfile.university,
            joinedAt = seminarParticipant.joinedAt,
            isActive = seminarParticipant.isActive,
            droppedAt = seminarParticipant.droppedAt
        )
    }

    data class UserControllerResponse(
        val id: Long?,
        val university: String?,
        val accepted: Boolean?,
        val seminars: List<SeminarParticipantDto.Response>,
    ) {
        constructor(participantProfile: ParticipantProfile): this(
            id = participantProfile.id,
            university = participantProfile.university,
            accepted = participantProfile.accepted,
            seminars = participantProfile.seminars.map {
                SeminarParticipantDto.Response(it)
            }

        )
    }
}