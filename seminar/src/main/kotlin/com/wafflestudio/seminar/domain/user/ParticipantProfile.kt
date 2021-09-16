package com.wafflestudio.seminar.domain.user

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.seminar.SeminarParticipant
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.OneToMany

@Entity
class ParticipantProfile (

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "participant")
    val seminars: List<SeminarParticipant> = listOf(),

    ) : BaseTimeEntity()