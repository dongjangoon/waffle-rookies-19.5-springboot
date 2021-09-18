package com.wafflestudio.seminar.domain.user.model

enum class Role(
    val role: String
    ){

    PARTICIPANT("participant"),
    INSTRUCTOR("instructor"),
    BOTH("participant, instructor")

}