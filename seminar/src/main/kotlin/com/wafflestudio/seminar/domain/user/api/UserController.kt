package com.wafflestudio.seminar.domain.user.api

import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import com.wafflestudio.seminar.domain.user.service.UserService
import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.exception.UserNotFoundException
import com.wafflestudio.seminar.global.auth.CurrentUser
import com.wafflestudio.seminar.global.auth.JwtTokenProvider
import com.wafflestudio.seminar.global.auth.dto.LoginRequest
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val userRepository: UserRepository,
    private val jwtTokenProvider: JwtTokenProvider
) {
    // TODO: 2021-09-16 error 발생시 200으로 리턴되는 문제
    @PostMapping("/")
    fun signup(@Valid @RequestBody signupRequest: UserDto.SignupRequest): ResponseEntity<UserDto.Response> {
        val user = userService.signup(signupRequest)
        return ResponseEntity.created(URI.create("/"))
            .header("Authentication", jwtTokenProvider.generateToken(user.email))
            .body(UserDto.Response(user))
    }

    // TODO: 2021-09-17 지연 로딩할 때 트랜잭션을 벗어나서 조회하는 문제, fetch join 써보자
    @PostMapping("/signin/")
    fun login(@RequestBody loginRequest: LoginRequest): UserDto.Response? {
        val user = userService.findUserByEmail(loginRequest.email)
        return user?.let { UserDto.Response(it) }
    }

    @GetMapping("/{user_id}/")
    fun getUserById(@PathVariable("user_id") user_id: Long): UserDto.Response {
        val user = userRepository.findByIdOrNull(user_id) ?: throw UserNotFoundException("NO USER")
        return UserDto.Response(user)
    }

    @GetMapping("/me/")
    fun getCurrentUser(@CurrentUser user: User): UserDto.Response {
        return UserDto.Response(user)
    }
}