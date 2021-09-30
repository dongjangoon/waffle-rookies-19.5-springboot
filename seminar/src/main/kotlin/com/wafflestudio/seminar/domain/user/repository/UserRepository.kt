package com.wafflestudio.seminar.domain.user.repository

import com.wafflestudio.seminar.domain.user.model.User
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.util.*

interface UserRepository: JpaRepository<User, Long?> {

    @EntityGraph(attributePaths = ["participantProfile", "instructorProfile", "participantProfile.seminars"])
    fun findByEmail(email: String?): User?
    fun existsByEmail(email: String): Boolean

    @EntityGraph(attributePaths = ["participantProfile", "instructorProfile", "participantProfile.seminars"])
    override fun findById(id: Long): Optional<User>
}
