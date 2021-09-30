package com.wafflestudio.seminar.domain.seminar.model

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern

@Entity
@Table(name = "seminars")
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
    @field:Pattern(regexp = "^([1-9]|[01][0-9]|2[0-3]):([0-5][0-9])$")
    var time: String,

    @Column
    var online: Boolean = true,

    /**
     * Seminar 가 없어지면 SeminarParticipant 도 같이 없어지도록 합니다.
     */
    @OneToMany(cascade = [CascadeType.REMOVE], mappedBy = "seminar")
    val seminarParticipants: MutableSet<SeminarParticipant> = mutableSetOf(),

    @OneToMany(cascade = [CascadeType.ALL], mappedBy = "seminar")
    var instructorProfile: MutableSet<InstructorProfile> = mutableSetOf(),

    ) : BaseTimeEntity()