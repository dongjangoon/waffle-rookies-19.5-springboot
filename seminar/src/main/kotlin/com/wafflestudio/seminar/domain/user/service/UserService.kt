package com.wafflestudio.seminar.domain.user.service

import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.exception.EmailAlreadyExistException
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
     * service logic 에서 트랜잭션이 일어나므로
     * user 를 여기서 반환
     */
    fun saveUser(user: User): User? {
        if (userRepository.findEmailList()?.contains(user.email) == true) {
            throw EmailAlreadyExistException()
        }

        return userRepository.save(user)
    }

    /**
     * findByIdOrNull 의 반환형이 Optional 이므로 null 검사를 하고 아닐시
     * UserNotFoundException throw
     */
    fun getUserById(id: Long): User? {
        return userRepository.findByIdOrNull(id) ?: throw UserNotFoundException()
    }

}