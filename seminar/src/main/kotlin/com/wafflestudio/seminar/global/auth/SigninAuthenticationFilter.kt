package com.wafflestudio.seminar.global.auth

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.jsonMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import com.wafflestudio.seminar.domain.user.dto.UserDto
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.global.auth.dto.LoginRequest
import com.wafflestudio.seminar.global.auth.model.UserPrincipalDetailService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher
import java.io.BufferedReader
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class SigninAuthenticationFilter(
    authenticationManager: AuthenticationManager?,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userPrincipalDetailService: UserPrincipalDetailService
) : UsernamePasswordAuthenticationFilter(authenticationManager) {
    init {
        setRequiresAuthenticationRequestMatcher(AntPathRequestMatcher("/api/v1/users/signin/", "POST"))
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain,
        authResult: Authentication,
    ) {
        response.addHeader("Authentication", jwtTokenProvider.generateToken(authResult))
        response.status = HttpServletResponse.SC_OK

        // 뭔가 너저분..
        response.contentType = "application/json"
        response.characterEncoding = "utf-8"
        val userEmail = authResult.name
        val user = userPrincipalDetailService.findUser(userEmail)
        val mapper = ObjectMapper().registerKotlinModule()
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        val responseBody = mapper.writeValueAsString(UserDto.Response(user))
        response.writer.write(responseBody)
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        super.unsuccessfulAuthentication(request, response, failed);
        response.status = HttpServletResponse.SC_UNAUTHORIZED
    }

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {
        // Parse auth request
        val parsedRequest: LoginRequest = parseRequest(request)
        val authRequest: Authentication =
            UsernamePasswordAuthenticationToken(parsedRequest.email, parsedRequest.password)
        return authenticationManager.authenticate(authRequest)
    }

    private fun parseRequest(request: HttpServletRequest): LoginRequest {
        val reader: BufferedReader = request.reader
        val objectMapper = ObjectMapper()
        return objectMapper.readValue(reader, LoginRequest::class.java)
    }

}