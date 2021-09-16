package com.wafflestudio.seminar.domain.user

import com.wafflestudio.seminar.domain.model.BaseEntity
import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.seminar.SeminarParticipant
import jdk.jfr.BooleanFlag
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.NotBlank

@Entity
class ParticipantProfile (

    @Column
    val university: String = "",

    @Column
    val accepted: Boolean = true,

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "participant")
    val seminars: List<SeminarParticipant> = listOf(),

    ) : BaseTimeEntity()