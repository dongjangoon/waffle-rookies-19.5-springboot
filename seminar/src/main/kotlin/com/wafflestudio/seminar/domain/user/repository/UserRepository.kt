package com.wafflestudio.seminar.domain.user.repository

import com.wafflestudio.seminar.domain.user.model.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UserRepository: JpaRepository<User, Long?> {

    @EntityGraph(attributePaths = ["participantProfile", "instructorProfile", "participantProfile.seminars", "participantProfile.seminars.seminar"])
    fun findByEmail(email: String?): User?
    fun existsByEmail(email: String): Boolean

    @EntityGraph(attributePaths = ["participantProfile", "instructorProfile", "participantProfile.seminars"])
    override fun findById(id: Long): Optional<User>
}
