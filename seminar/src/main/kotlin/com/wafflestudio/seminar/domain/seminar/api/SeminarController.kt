package com.wafflestudio.seminar.domain.seminar.api

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.seminar.service.SeminarService
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.global.auth.CurrentUser
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/seminars")
class SeminarController(
    private val seminarService: SeminarService,
) {
    /**
     * response 에서 어차피 participants 는 여기서 0명, instructor 도 한명만 고려해주면 됨
     */
    @PostMapping("/")
    fun register(@RequestBody @Valid registerRequest: SeminarDto.RegisterRequest, @CurrentUser user: User): ResponseEntity<SeminarDto.Response> {
        val seminar = seminarService.register(registerRequest, user)
        return ResponseEntity
            .status(201)
            .body(SeminarDto.Response(seminar))
    }

}