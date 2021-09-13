package com.wafflestudio.seminar.domain.user.repository

import com.wafflestudio.seminar.domain.user.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface UserRepository: JpaRepository<User, Long?> {
    // 인자로 들어오는 이메일이 있는지 찾는 함수입니다.
    @Query("select u.email from User u")
    fun findEmailList(): List<String>?
}