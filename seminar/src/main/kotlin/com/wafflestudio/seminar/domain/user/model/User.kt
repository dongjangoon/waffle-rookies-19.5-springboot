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
    var roles: String = "",

    /**
     * cascade Type 을 ALL 로 지정해주어서 USER 영속성 전이 시에 participantProfile 이나
     * InstructorProfile 이 같이 영속성 전이 됩니다.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "participant_id", referencedColumnName = "id")
    var participantProfile: ParticipantProfile? = null,

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name = "instructor_id", referencedColumnName = "id")
    val instructorProfile: InstructorProfile? = null,

    ) : BaseEntity()
