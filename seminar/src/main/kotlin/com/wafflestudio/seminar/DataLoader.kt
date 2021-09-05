package com.wafflestudio.seminar

import com.wafflestudio.seminar.domain.os.model.OperatingSystem
import com.wafflestudio.seminar.domain.os.repository.OperatingSystemRepository
import com.wafflestudio.seminar.domain.survey.model.SurveyResponse
import com.wafflestudio.seminar.domain.survey.repository.SurveyResponseRepository
import com.wafflestudio.seminar.domain.user.model.User
import com.wafflestudio.seminar.domain.user.repository.UserRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import java.io.BufferedReader
import java.io.FileReader
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@Component
class DataLoader(
    private val operatingSystemRepository: OperatingSystemRepository,
    private val surveyResponseRepository: SurveyResponseRepository,
    private val userRepository: UserRepository,
) : ApplicationRunner {
    // 어플리케이션 동작 시 실행
    override fun run(args: ApplicationArguments) {
        val windows = OperatingSystem(name = "Windows", price = 200000, description = "Most favorite OS in South Korea")
        val macos =
            OperatingSystem(name = "MacOS", price = 300000, description = "Most favorite OS of Seminar Instructors")
        val linux = OperatingSystem(name = "Linux", price = 0, description = "Linus Benedict Torvalds")
        operatingSystemRepository.save(windows)
        operatingSystemRepository.save(macos)
        operatingSystemRepository.save(linux)

        BufferedReader(FileReader(ClassPathResource("data/example_surveyresult.tsv").file)).use { br ->
            br.lines().forEach {
                val rawSurveyResponse = it.split("\t")
                val newSurveyResponse = SurveyResponse(
                    timestamp = LocalDateTime.parse(rawSurveyResponse[0], DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                    os = operatingSystemRepository.findByNameEquals(rawSurveyResponse[1]),
                    springExp = rawSurveyResponse[2].toInt(),
                    rdbExp = rawSurveyResponse[3].toInt(),
                    programmingExp = rawSurveyResponse[4].toInt(),
                    major = rawSurveyResponse[5],
                    grade = rawSurveyResponse[6]
                )
                surveyResponseRepository.save(newSurveyResponse)
            }
        }

        // 테스트용 유저 삽입
        val user1 = User(name = "동숙이", email = "dongsuk@suk.com")
        val user2 = User(name = "현숙이", email = "hyunsuk@suk.com")
        val user3 = User(name = "철숙이", email = "chulsuk@suk.com")
        val user4 = User(name = "대숙이", email = "daesuk@suk.com")
        userRepository.save(user1)
        userRepository.save(user2)
        userRepository.save(user3)
        userRepository.save(user4)

    }
}
