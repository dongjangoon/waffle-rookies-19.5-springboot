package com.wafflestudio.seminar.domain.user.dto

import com.wafflestudio.seminar.domain.user.model.User

class InstructorSeminarResponseDto {
    data class Response(
        val id: Long?,
        val name: String?,
        val email: String?,
        val company: String?,
    ) {
        constructor(user: User?): this(
            id = user?.id,
            name = user?.name,
            email = user?.email,
            company = user?.instructorProfile?.company,
        )
    }
}