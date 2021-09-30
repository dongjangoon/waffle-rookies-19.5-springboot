package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name = "participants")
class ParticipantProfile (

    @OneToOne(mappedBy = "participantProfile")
    val user: User?,

    @Column
    var university: String = "",

    @Column
    var accepted: Boolean = true,

    @Column(name = "seminars")
    @OneToMany(fetch = FetchType.LAZY, cascade = [CascadeType.ALL], mappedBy = "participantProfile")
    val seminars: MutableList<SeminarParticipant> = mutableListOf(),

    ) : BaseTimeEntity(), Serializable