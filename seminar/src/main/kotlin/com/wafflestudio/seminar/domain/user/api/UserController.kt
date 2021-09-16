package com.wafflestudio.seminar.domain.user.api

import com.wafflestudio.seminar.domain.user.UserService
import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.global.auth.JwtTokenProvider
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletResponse
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService,
    private val jwtTokenProvider: JwtTokenProvider
) {
    // TODO: 2021-09-16 error 발생시 200으로 리턴되는 문제
    @PostMapping("/")
    @ResponseStatus(HttpStatus.CREATED)
    fun signup(@RequestBody @Valid signupRequest: UserDto.SignupRequest, response: HttpServletResponse): UserDto.Response {
        val user = userService.signup(signupRequest)
        response.addHeader("Authentication", jwtTokenProvider.generateToken(user.email))
        return UserDto.Response(user)
    }
}