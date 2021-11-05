package com.wafflestudio.seminar.domain.seminar.dto

import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import java.time.LocalDateTime

class SeminarParticipantDto {
    data class Response(
        val id: Long,
        val name: String,
        val joinedAt: LocalDateTime?,
        val isActive: Boolean,
        val droppedAt: LocalDateTime?
    ) {
        constructor(seminarParticipant: SeminarParticipant): this(
            id = seminarParticipant.seminar.id,
            name = seminarParticipant.seminar.name,
            joinedAt = seminarParticipant.joinedAt,
            isActive = seminarParticipant.isActive,
            droppedAt = seminarParticipant.droppedAt
        )
    }
}