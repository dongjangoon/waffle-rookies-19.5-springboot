package com.wafflestudio.seminar.domain.user.repository

import com.wafflestudio.seminar.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository: JpaRepository<User, Long?> {
}