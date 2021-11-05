package com.wafflestudio.seminar.domain.seminar.repository

import com.wafflestudio.seminar.domain.seminar.model.Seminar
import org.springframework.data.jpa.repository.EntityGraph
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SeminarRepository: JpaRepository<Seminar, Long> {

    fun findByName(name: String): Seminar

    @EntityGraph(attributePaths = ["instructorProfile", "instructorProfile.user", "seminarParticipants", "seminarParticipants.participantProfile"])
    fun findByNameContainingOrderByCreatedAtDesc(name: String): List<Seminar>

    @EntityGraph(attributePaths = ["instructorProfile", "instructorProfile.user", "seminarParticipants", "seminarParticipants.participantProfile"])
    fun findAllByOrderByCreatedAtAsc(): List<Seminar>

    @EntityGraph(attributePaths = ["instructorProfile", "instructorProfile.user", "seminarParticipants", "seminarParticipants.participantProfile"])
    fun findAllByOrderByCreatedAtDesc(): List<Seminar>

    @EntityGraph(attributePaths = ["instructorProfile", "instructorProfile.user", "seminarParticipants", "seminarParticipants.participantProfile"])
    fun findByNameContainingOrderByCreatedAtAsc(name: String): List<Seminar>
}