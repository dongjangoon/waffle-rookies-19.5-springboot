package com.wafflestudio.seminar.domain.user.model

import com.sun.istack.NotNull
import javax.persistence.*
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
    @Column(unique = true)
    var email: String = "",
)