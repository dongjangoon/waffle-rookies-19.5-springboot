package com.wafflestudio.seminar.domain.seminar

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import javax.persistence.*

@Entity
class SeminarParticipant (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", referencedColumnName = "id")
    val seminar: Seminar,

    ) : BaseTimeEntity()