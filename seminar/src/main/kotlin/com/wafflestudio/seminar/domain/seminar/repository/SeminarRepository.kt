package com.wafflestudio.seminar.domain.seminar.repository

import com.wafflestudio.seminar.domain.seminar.model.Seminar
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SeminarRepository: JpaRepository<Seminar, Long> {
    fun findByNameContainingOrderByCreatedAtDesc(name: String): List<Seminar>
    fun findAllByOrderByCreatedAtAsc(): List<Seminar>
    fun findAllByOrderByCreatedAtDesc(): List<Seminar>

//    @Query("select s from Seminar s where s.name = :name order by s.createdAt asc")
    fun findByNameContainingOrderByCreatedAtAsc(name: String): List<Seminar>
}