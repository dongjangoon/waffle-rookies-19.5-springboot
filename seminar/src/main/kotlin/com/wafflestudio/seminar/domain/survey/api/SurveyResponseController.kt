package com.wafflestudio.seminar.domain.survey.api

import com.wafflestudio.seminar.domain.survey.dto.SurveyResponseDto
import com.wafflestudio.seminar.domain.os.exception.OsNotFoundException
import com.wafflestudio.seminar.domain.os.service.OperatingSystemService
import com.wafflestudio.seminar.domain.survey.exception.SurveyNotFoundException
import com.wafflestudio.seminar.domain.survey.model.SurveyResponse
import com.wafflestudio.seminar.domain.survey.service.SurveyResponseService
import com.wafflestudio.seminar.domain.user.service.UserService
import org.modelmapper.ModelMapper
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid
import kotlin.reflect.typeOf

@RestController
@RequestMapping("/api/v1/results")
class SurveyResponseController(
    private val surveyResponseService: SurveyResponseService,
    private val userService: UserService,
    private val operatingSystemService: OperatingSystemService,
    private val modelMapper: ModelMapper
) {
    @GetMapping("/")
    fun getSurveyResponses(@RequestParam(required = false) os: String?): ResponseEntity<List<SurveyResponseDto.Response>> {
        return try {
            val surveyResponses =
                if (os != null) surveyResponseService.getSurveyResponsesByOsName(os)
                else surveyResponseService.getAllSurveyResponses()
            val responseBody = surveyResponses.map { modelMapper.map(it, SurveyResponseDto.Response::class.java) }
            ResponseEntity.ok(responseBody)
        } catch (e: OsNotFoundException) {
            ResponseEntity.notFound().build()
        }
        // AOP를 적용해 exception handling을 따로 하도록 고쳐보셔도 됩니다.
    }

    @GetMapping("/{id}/")
    fun getSurveyResponse(@PathVariable("id") id: Long): ResponseEntity<SurveyResponseDto.Response> {
        return try {
            val surveyResponse = surveyResponseService.getSurveyResponseById(id)
            val responseBody = modelMapper.map(surveyResponse, SurveyResponseDto.Response::class.java)
            ResponseEntity.ok(responseBody)
        } catch (e: SurveyNotFoundException) {
            ResponseEntity.notFound().build()
        }
    }

    /**
     * request 와 modelMapper 로 만든 SurveyResponse 엔티티에 os, user 를 전해주었습니다.
     * 서비스 단에서 영속화한 뒤, 반환된 surveyResponse 를 responseBody 로 전달했습니다.
     */
    @PostMapping("/")
    fun addSurveyResponse(
        @ModelAttribute @Valid body: SurveyResponseDto.CreateRequest,
        @RequestHeader("User-Id") userId: Long
    ): ResponseEntity<SurveyResponseDto.Response> {
        //TODO: API 생성
        return try {
            val os = operatingSystemService.getOperatingSystemByName(body.os)
            val user = userService.getUserById(userId)
            val newSurveyResponse = modelMapper.map(body, SurveyResponse::class.java)
            newSurveyResponse.os = os
            newSurveyResponse.user = user

            val surveyResponse = surveyResponseService.saveSurveyResponse(newSurveyResponse)
            val responseBody = modelMapper.map(surveyResponse, SurveyResponseDto.Response::class.java)
            ResponseEntity(responseBody, HttpStatus.CREATED)
        } catch (e: OsNotFoundException) {
            ResponseEntity.notFound().build()
        }

    }

    @PatchMapping("/{id}/")
    fun modifySurveyResponseWithId(@ModelAttribute @Valid body: SurveyResponseDto.ModifyRequest): SurveyResponseDto.Response {
        //TODO: API 생성
        return SurveyResponseDto.Response()
    }
}
