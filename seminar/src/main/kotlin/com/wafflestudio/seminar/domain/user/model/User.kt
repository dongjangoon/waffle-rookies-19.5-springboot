package com.wafflestudio.seminar.domain.user.model

import com.wafflestudio.seminar.domain.model.BaseEntity
import javax.persistence.*
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull

@Entity
@Table(name = "seminar_user")
class User(
    @Column
    @field:NotBlank
    val name: String,

    @Column(unique = true)
    @field:NotBlank
    val email: String,

    @Column
    @field:NotBlank
    val password: String,

    @Column
    @field:NotNull
    val roles: String = "",

    /**
     * cascade Type 을 ALL 로 지정해주어서 USER 영속성 전이 시에 participantProfile 이나
     * InstructorProfile 이 같이 영속성 전이 됩니다.
     */
        @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @JoinColumns(
            JoinColumn(name = "participant_id", referencedColumnName = "id"),
            JoinColumn(name = "university", referencedColumnName = "university"),
            JoinColumn(name = "accepted", referencedColumnName = "accepted")
        )
        val participantProfile: ParticipantProfile? = null,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
        @JoinColumns(
            JoinColumn(name = "instructor_id", referencedColumnName = "id"),
            JoinColumn(name = "company", referencedColumnName = "company"),
            JoinColumn(name = "year", referencedColumnName = "year")
        )
        val instructorProfile: InstructorProfile? = null,

    ) : BaseEntity()
