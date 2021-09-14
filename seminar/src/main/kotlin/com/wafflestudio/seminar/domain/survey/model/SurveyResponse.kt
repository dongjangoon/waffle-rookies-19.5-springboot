package com.wafflestudio.seminar.domain.survey.model

import com.wafflestudio.seminar.domain.os.model.OperatingSystem
import com.wafflestudio.seminar.domain.user.model.User
import java.time.LocalDateTime
import javax.persistence.*
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull


@Entity
class SurveyResponse(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @ManyToOne
    @JoinColumn(name = "os_id", referencedColumnName = "id")
    @NotNull
    var os: OperatingSystem? = null,

    // user 추가한 부분
    // SurveyResponse 엔티티의 영속성 전이가 이루어질 때 user 엔티티의 영속성 전이도 같이 이루어집니다
    // (둘 다 데이터베이스에 저장된다)
    // user row 가 삭제되어도 영향 x
    @ManyToOne(cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    var user: User? = null,

    @Column(name = "spring_exp")
    @field:NotNull
    @field:Min(1, message = "The value must be between 1 and 5")
    @field:Max(5, message = "The value must be between 1 and 5")
    var springExp: Int? = null,

    @Column(name = "rdb_exp")
    @field:NotNull
    @field:Min(1, message = "The value must be between 1 and 5")
    @field:Max(5, message = "The value must be between 1 and 5")
    var rdbExp: Int? = null,

    @Column(name = "programming_exp")
    @field:NotNull
    @field:Min(1, message = "The value must be between 1 and 5")
    @field:Max(5, message = "The value must be between 1 and 5")
    var programmingExp: Int? = null,

    @NotBlank
    var major: String? = null,

    @NotBlank
    var grade: String? = null,

    @Column(name = "backend_reason")
    var backendReason: String? = null,
    @Column(name = "waffle_reason")
    var waffleReason: String? = null,

    @Column(name = "something_to_say")
    var somethingToSay: String? = null,

    @NotNull
    var timestamp: LocalDateTime = LocalDateTime.now(),
)
