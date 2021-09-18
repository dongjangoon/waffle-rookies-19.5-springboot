package com.wafflestudio.seminar.domain.seminar.repository

import com.wafflestudio.seminar.domain.seminar.model.Seminar
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SeminarRepository: JpaRepository<Seminar, Long> {
}