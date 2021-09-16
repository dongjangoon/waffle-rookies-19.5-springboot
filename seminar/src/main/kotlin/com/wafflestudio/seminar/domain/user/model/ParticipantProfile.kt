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
    val university: String = "",

    @Column
    val accepted: Boolean = true,

    // OneToMany annotation 때문에 Serializable 상속
    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "participant")
    val seminars: List<SeminarParticipant> = listOf(),

    ) : BaseTimeEntity(), Serializable