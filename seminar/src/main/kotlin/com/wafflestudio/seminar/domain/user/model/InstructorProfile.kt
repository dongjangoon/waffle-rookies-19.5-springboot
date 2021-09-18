package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.seminar.Seminar
import java.io.Serializable
import javax.persistence.*

@Entity
class InstructorProfile (

    @Column
    var company: String = "",

    @Column
    var year: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", referencedColumnName = "id")
    val seminar: Seminar? = null,

    ) : BaseTimeEntity(), Serializable