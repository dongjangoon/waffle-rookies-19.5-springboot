package com.wafflestudio.seminar.domain.seminar.dto

import com.wafflestudio.seminar.domain.seminar.model.Seminar
import com.wafflestudio.seminar.domain.user.dto.InstructorProfileDto
import com.wafflestudio.seminar.domain.user.dto.ParticipantProfileDto
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
        var instructors: MutableList<InstructorProfileDto.SeminarResponse>,
        var participants: MutableList<ParticipantProfileDto.SeminarResponse>
    ) {
        constructor(seminar: Seminar) : this(
            id = seminar.id,
            name = seminar.name,
            capacity = seminar.capacity,
            count = seminar.count,
            time = seminar.time,
            online = seminar.online,
            instructors = seminar.instructorProfile.map{
                InstructorProfileDto.SeminarResponse(it.user)
            } as MutableList<InstructorProfileDto.SeminarResponse>,
            participants = seminar.seminarParticipants.map{
                ParticipantProfileDto.SeminarResponse(it.participantProfile, it)
            } as MutableList<ParticipantProfileDto.SeminarResponse>,
        )
    }

    data class ChargeResponse(
        val id: Long,
        val name: String,
    ) {
        constructor(seminar: Seminar) : this(
            id = seminar.id,
            name = seminar.name,
        )
    }

    data class QueryResponse(
        val id: Long,
        val name: String,
        val capacity: Int,
        val count: Int,
        val time: String,
        val online: Boolean,
        var instructors: MutableList<InstructorProfileDto.SeminarResponse>,
        var participants: MutableList<ParticipantProfileDto.SeminarResponse>,
        val participant_count: Int,
    ) {
        constructor(seminar: Seminar) : this(
            id = seminar.id,
            name = seminar.name,
            capacity = seminar.capacity,
            count = seminar.count,
            time = seminar.time,
            online = seminar.online,
            instructors = seminar.instructorProfile.map{
                InstructorProfileDto.SeminarResponse(it.user)
            } as MutableList<InstructorProfileDto.SeminarResponse>,
            participants = seminar.seminarParticipants.map{
                ParticipantProfileDto.SeminarResponse(it.participantProfile, it)
            } as MutableList<ParticipantProfileDto.SeminarResponse>,
            participant_count = seminar.seminarParticipants.size - seminar.seminarParticipants.count { !it.isActive }
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

    data class UpdateRequest(

        @field:Min(1, message = "INPUT POSITIVE INTEGER")
        val capacity: Int?,

        @field:Min(1, message = "INPUT POSITIVE INTEGER")
        val count: Int?,
        val time: String?,
        val online: String?,
    )

    data class EnterRequest(
        @field:NotBlank
        val role: String,
    )
}