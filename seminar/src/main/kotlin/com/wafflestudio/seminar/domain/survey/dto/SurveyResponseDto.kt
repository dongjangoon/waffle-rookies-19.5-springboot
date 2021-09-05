package com.wafflestudio.seminar.domain.survey.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.wafflestudio.seminar.domain.os.dto.OperatingSystemDto
import com.wafflestudio.seminar.domain.os.model.OperatingSystem
import com.wafflestudio.seminar.domain.user.model.User
import java.time.LocalDateTime
import javax.persistence.Column
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

class SurveyResponseDto {
    data class Response(
        var id: Long? = 0,
        var os: OperatingSystem? = null,
        var user: User? = null,
        var springExp: Int = 0,
        var rdbExp: Int = 0,
        var programmingExp: Int = 0,
        var major: String? = "",
        var grade: String? = "",
        var backendReason: String? = "",
        var waffleReason: String? = "",
        var somethingToSay: String? = "",
        var timestamp: LocalDateTime? = null

    )

    // TODO: 아래 두 DTO 완성
    data class CreateRequest(
        @field:NotNull
        @field:NotBlank
        var os: String = "",

        @field:NotNull
        @Min(0, message = "The value must be between 1 and 5")
        @Max(5, message = "The value must be between 1 and 5")
        var springExp: Int = 0,

        @field:NotNull
        @Min(0, message = "The value must be between 1 and 5")
        @Max(5, message = "The value must be between 1 and 5")
        var rdbExp: Int = 0,

        @field:NotNull
        @Min(0, message = "The value must be between 1 and 5")
        @Max(5, message = "The value must be between 1 and 5")
        var programmingExp: Int = 0,

        var major: String? = "",
        var grade: String? = "",
        var backendReason: String? = "",
        var waffleReason: String? = "",
        var somethingToSay: String? = "",
        var timestamp: LocalDateTime? = LocalDateTime.now(),
    )

    data class ModifyRequest(
        var something: String? = ""
        // 예시 - 지우고 새로 생성
    )
}
