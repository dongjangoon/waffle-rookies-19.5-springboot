package com.wafflestudio.seminar.domain.user.dto

import com.wafflestudio.seminar.domain.user.model.User

/**
 * null 처리 이게 맞나..?
 */
class ParticipantSeminarResponseDto {
    data class Response(
        val id: Long?,
        val name: String?,
        val email: String?,
        val university: String?,
    ) {
        constructor(user: User?): this(
            id = user?.id,
            name = user?.name,
            email = user?.email,
            university = user?.participantProfile?.university,
        )
    }
}