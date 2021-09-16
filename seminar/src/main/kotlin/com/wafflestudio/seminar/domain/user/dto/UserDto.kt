package com.wafflestudio.seminar.domain.user.dto

import com.wafflestudio.seminar.domain.os.dto.OperatingSystemDto
import com.wafflestudio.seminar.domain.survey.model.SurveyResponse
import com.wafflestudio.seminar.domain.user.InstructorProfile
import com.wafflestudio.seminar.domain.user.ParticipantProfile
import com.wafflestudio.seminar.domain.user.User
import java.time.LocalDateTime
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

class UserDto {
    data class Response(
        val id: Long,
        val name: String,
        val email: String,
        val participantProfile: ParticipantProfile?,
        val instructorProfile: InstructorProfile?
    ) {
        constructor(user: User) : this(
            id = user.id,
            name = user.name,
            email = user.email,
            participantProfile = user.participantProfile,
            instructorProfile = user.instructorProfile
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
}