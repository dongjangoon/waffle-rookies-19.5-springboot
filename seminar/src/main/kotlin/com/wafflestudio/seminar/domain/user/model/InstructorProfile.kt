package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.seminar.model.Seminar
import java.io.Serializable
import javax.persistence.*

@Entity
class InstructorProfile (

    @OneToOne(mappedBy = "instructorProfile")
    val user: User?,

    @Column
    var company: String = "",

    @Column
    var year: Int? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", referencedColumnName = "id")
    var seminar: Seminar? = null,

    ) : BaseTimeEntity(), Serializable