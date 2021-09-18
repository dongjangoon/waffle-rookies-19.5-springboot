package com.wafflestudio.seminar.domain.seminar

import com.wafflestudio.seminar.domain.model.BaseTimeEntity
import com.wafflestudio.seminar.domain.user.model.InstructorProfile
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.OneToMany

@Entity
class Seminar (

    /**
     * Seminar 가 없어지면 SeminarParticipant 도 같이 없어지도록 합니다.
     */
    @OneToMany(cascade = [CascadeType.REMOVE], mappedBy = "seminar")
    val seminarParticipants: List<SeminarParticipant> = listOf(),

    @OneToMany(mappedBy = "seminar")
    val instructorProfile: List<InstructorProfile> = listOf(),

    ) : BaseTimeEntity()