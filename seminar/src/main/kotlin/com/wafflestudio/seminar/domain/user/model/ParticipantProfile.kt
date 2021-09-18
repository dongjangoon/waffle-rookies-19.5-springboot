package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.seminar.model.SeminarParticipant
import java.io.Serializable
import javax.persistence.*

@Entity
class ParticipantProfile (

    @OneToOne(mappedBy = "instructorProfile")
    val user: User?,

    @Column
    var university: String = "",

    @Column
    var accepted: Boolean = true,

    // OneToMany annotation 때문에 Serializable 상속
    // TODO: 2021-09-17 fetch join? lazy 하게 받아올 방법 생각
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE], mappedBy = "participantProfile")
    val seminars: List<SeminarParticipant> = listOf(),

    ) : BaseTimeEntity(), Serializable