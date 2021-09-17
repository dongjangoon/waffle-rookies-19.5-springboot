package com.wafflestudio.seminar.domain.seminar

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.user.model.ParticipantProfile
import javax.persistence.*

@Entity
class SeminarParticipant (

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seminar_id", referencedColumnName = "id")
    val seminar: Seminar,

    @ManyToOne
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    val participant: ParticipantProfile,

    ) : BaseTimeEntity()