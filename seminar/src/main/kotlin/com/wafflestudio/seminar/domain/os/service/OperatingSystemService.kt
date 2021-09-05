package com.wafflestudio.seminar.domain.os.service

import com.wafflestudio.seminar.domain.os.dto.OperatingSystemDto
import com.wafflestudio.seminar.domain.os.exception.OsNotFoundException
import com.wafflestudio.seminar.domain.os.model.OperatingSystem
import com.wafflestudio.seminar.domain.os.repository.OperatingSystemRepository
import org.modelmapper.ModelMapper
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class OperatingSystemService(
    private val operatingSystemRepository: OperatingSystemRepository,
) {
    fun getAllOperatingSystems(): List<OperatingSystem> {
        return operatingSystemRepository.findAll()
    }

    fun getOperatingSystemById(id: Long): OperatingSystem {
        return operatingSystemRepository.findByIdOrNull(id) ?: throw OsNotFoundException()
    }

    /**
     * 설문조사를 추가할 때 쓰이고 request 로 받은 os 가 존재하지 않으면 OsNotFoundException 을 발생시킵니다.
     */
    fun getOperatingSystemByName(name: String): OperatingSystem {
        return operatingSystemRepository.findByNameEquals(name) ?: throw OsNotFoundException()
    }
}