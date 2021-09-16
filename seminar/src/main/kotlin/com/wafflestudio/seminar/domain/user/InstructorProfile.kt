package com.wafflestudio.seminar.domain.user

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.seminar.Seminar
import javax.persistence.*

@Entity
class InstructorProfile (

    @Column
    val company: String = "",

    @Column
    val year: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", referencedColumnName = "id")
    val seminar: Seminar? = null,

    ) : BaseTimeEntity()