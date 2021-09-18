package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseEntity
import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.seminar.SeminarParticipant
import jdk.jfr.BooleanFlag
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class ParticipantProfile (

    @Column
    var university: String = "",

    @Column
    var accepted: Boolean = true,

    // OneToMany annotation 때문에 Serializable 상속
    // TODO: 2021-09-17 fetch join? lazy 하게 받아올 방법 생각
    @OneToMany(fetch = FetchType.EAGER, cascade = [CascadeType.REMOVE], mappedBy = "participantProfile")
    val seminars: List<SeminarParticipant> = listOf(),

    ) : BaseTimeEntity(), Serializable