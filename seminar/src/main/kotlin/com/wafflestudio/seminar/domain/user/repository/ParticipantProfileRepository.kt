package com.wafflestudio.seminar.domain.user.repository

import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ParticipantProfileRepository: JpaRepository<ParticipantProfile, Long> {
}