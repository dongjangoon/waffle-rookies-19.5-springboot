package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.exception.UserNotFoundException
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    /**
     * findByIdOrNull 의 반환형이 Optional 이므로 null 검사를 하고 아닐시
     * UserNotFoundException throw
     */
    fun getUserById(id: Long): User? {
        return userRepository.findByIdOrNull(id) ?: throw UserNotFoundException()
    }

}