package com.wafflestudio.seminar.domain.user.dto

import com.sun.istack.NotNull
import com.wafflestudio.seminar.domain.user.model.User
import javax.validation.constraints.NotBlank

class UserDto {
    data class Response(
        var id: Long? = 0,
        var name: String = "",
        var email: String = ""
    )

    data class CreateRequest(
        @field:NotNull
        @field:NotBlank
        var name: String? = "",

        @field:NotNull
        @field:NotBlank
        var email: String? = ""
    )
}