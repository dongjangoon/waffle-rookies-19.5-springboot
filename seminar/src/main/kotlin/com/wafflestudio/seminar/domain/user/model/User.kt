package com.wafflestudio.seminar.domain.user.model

import com.sun.istack.NotNull
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank

@Entity
class User (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @NotNull
    @NotBlank
    var name: String = "",

    @NotNull
    @NotBlank
    @Email
    var email: String = "",
)