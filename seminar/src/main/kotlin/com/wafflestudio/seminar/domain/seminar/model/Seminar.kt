package com.wafflestudio.seminar.domain.seminar.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank

@Entity
class Seminar (
    @Column
    @field:NotBlank
    val name: String,

    @Column
    @field:Min(1, message = "INPUT POSITIVE INTEGER")
    var capacity: Int,

    @Column
    @field:Min(1, message = "INPUT POSITIVE INTEGER")
    var count: Int,

    @Column
    @field:NotBlank
    var time: String,

    @Column
    var online: Boolean = true,

    /**
     * Seminar 가 없어지면 SeminarParticipant 도 같이 없어지도록 합니다.
     */
    @OneToMany(cascade = [CascadeType.REMOVE], mappedBy = "seminar")
    val seminarParticipants: MutableList<SeminarParticipant> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.PERSIST], mappedBy = "seminar")
    var instructorProfile: MutableList<InstructorProfile> = mutableListOf(),

    ) : BaseTimeEntity()