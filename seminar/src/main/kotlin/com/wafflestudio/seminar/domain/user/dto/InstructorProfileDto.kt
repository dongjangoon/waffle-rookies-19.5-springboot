package com.wafflestudio.seminar.domain.user.dto

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import com.wafflestudio.seminar.domain.user.model.User

class InstructorProfileDto {
    data class SeminarResponse(
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

    data class UserControllerResponse(
        val id: Long?,
        val company: String?,
        val year: Int?,
        val charge: SeminarDto.ChargeResponse?,
    ) {
        constructor(instructorProfile: InstructorProfile): this(
            id = instructorProfile.id,
            company = instructorProfile.company,
            year = instructorProfile.year,
            charge = instructorProfile.seminar?.let { SeminarDto.ChargeResponse(it) }
        )
    }
}