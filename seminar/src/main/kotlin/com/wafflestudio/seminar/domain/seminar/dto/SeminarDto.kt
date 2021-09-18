package com.wafflestudio.seminar.domain.seminar.dto

import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.user.dto.InstructorSeminarResponseDto
import com.wafflestudio.seminar.domain.user.dto.ParticipantSeminarResponseDto
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

class SeminarDto {
    data class Response(
        val id: Long,
        val name: String,
        val capacity: Int,
        val count: Int,
        val time: String,
        val online: Boolean,
        var instructors: MutableList<InstructorSeminarResponseDto.Response>,
        var participants: MutableList<ParticipantSeminarResponseDto.Response>
    ) {
        constructor(seminar: Seminar) : this(
            id = seminar.id,
            name = seminar.name,
            capacity = seminar.capacity,
            count = seminar.count,
            time = seminar.time,
            online = seminar.online,
            instructors = seminar.instructorProfile.map{
                InstructorSeminarResponseDto.Response(it.user)
            } as MutableList<InstructorSeminarResponseDto.Response>,
            participants = seminar.seminarParticipants.map{
                ParticipantSeminarResponseDto.Response(it.participantProfile.user)
            } as MutableList<ParticipantSeminarResponseDto.Response>,
        )
    }

    data class RegisterRequest(
        @field:NotBlank
        val name: String,

        @field:Min(1, message = "INPUT POSITIVE INTEGER")
        val capacity: Int,

        @field:Min(1, message = "INPUT POSITIVE INTEGER")
        val count: Int,

        @field:NotBlank
        val time: String,
        val online: String?,
    )
}