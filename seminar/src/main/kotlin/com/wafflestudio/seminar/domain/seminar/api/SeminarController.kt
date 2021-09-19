package com.wafflestudio.seminar.domain.seminar.api

import com.wafflestudio.seminar.domain.seminar.dto.SeminarDto
import com.wafflestudio.seminar.domain.seminar.service.SeminarService
import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.global.auth.CurrentUser
import com.wafflestudio.seminar.global.common.dto.ListResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @PutMapping("/{seminar_id}/")
    fun update(@PathVariable("seminar_id") seminarId: Long,
               @RequestBody @Valid updateRequest: SeminarDto.UpdateRequest,
               @CurrentUser user: User): SeminarDto.Response {
        val seminar = seminarService.update(seminarId, updateRequest, user)
        return SeminarDto.Response(seminar)
    }

    @GetMapping("/{seminar_id}/")
    fun getSeminarById(@PathVariable("seminar_id") seminarId: Long): SeminarDto.Response {
        val seminar = seminarService.getSeminarById(seminarId)
        return SeminarDto.Response(seminar)
    }

    @GetMapping("/")
    fun getSeminarByQueryParam(@RequestParam allParams: Map<String, String>): ListResponse<SeminarDto.QueryResponse> {
        val seminars = seminarService.getSeminarsByQueryParams(allParams)
        val queryResponse = seminars.map {
            SeminarDto.QueryResponse(it)
        }

        return ListResponse(queryResponse)
    }

    @PostMapping("/{seminar_id}/user/")
    fun enterSeminarLater(@PathVariable("seminar_id") seminarId: Long,
                                @RequestBody @Valid enterRequest: SeminarDto.EnterRequest,
                                @CurrentUser user: User): ResponseEntity<SeminarDto.Response> {
        val seminar = seminarService.enterSeminarLater(seminarId, enterRequest, user)
        return ResponseEntity
            .status(201)
            .body(SeminarDto.Response(seminar))
    }
}