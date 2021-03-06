package com.wafflestudio.seminar.domain.user.dto

import com.wafflestudio.seminar.domain.user.model.User
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

class UserDto {
    data class Response(
        val id: Long,
        val name: String,
        val email: String,
        val participantProfile: ParticipantProfileDto.UserControllerResponse?,
        val instructorProfile: InstructorProfileDto.UserControllerResponse?,
    ) {
        constructor(user: User) : this(
            id = user.id,
            name = user.name,
            email = user.email,
            participantProfile = user.participantProfile?.let { ParticipantProfileDto.UserControllerResponse(it) },
            instructorProfile = user.instructorProfile?.let { InstructorProfileDto.UserControllerResponse(it) }
        )
    }

    data class SignupRequest(
        @field:NotBlank
        val name: String,
        @field:NotBlank
        val email: String,
        @field:NotBlank
        val password: String,
        @field:NotBlank
        val role : String,

        val university : String = "",
        val accepted : Boolean = true,

        val company: String = "",
        @field:Min(1, message = "You should input the positive integer.")
        val year: Int? = null
    )

    data class ModifyRequest(
        val university : String = "",

        val company: String = "",
        @field:Min(1, message = "You should input the positive integer.")
        val year: Int? = null
    )

    data class ParticipantRequest(
        val university: String = "",
        val accepted: Boolean = true
    )

}