package com.wafflestudio.seminar.domain.user.api

import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.service.UserService
import org.modelmapper.ModelMapper
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user/")
class UserController(
    private val userService: UserService,
    private val modelMapper: ModelMapper
) {
    /**
     * 들어오는 request body 를 받아 UserDto.CreateRequest 로 변환하고
     * service layer 로 넘겨주고 반환되는 user 를 modelMapper 를 이용해서
     * response 로 변환한 후 반환해줍니다.
     */
    @PostMapping("/")
    fun addUser(
        @ModelAttribute @Valid body: UserDto.CreateRequest
    ): UserDto.Response {
        val newUser = modelMapper.map(body, User::class.java)
        val response = modelMapper.map(userService.saveUser(newUser), UserDto.Response::class.java)
        return response
    }

}