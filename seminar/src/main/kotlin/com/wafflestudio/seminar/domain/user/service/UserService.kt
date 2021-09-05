package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository
) {

    /**
     * service logic 에서 트랜잭션이 일어나므로
     * user 를 여기서 반환
     */
    fun saveUser(user: User): User? {
        return userRepository.save(user)
    }

}