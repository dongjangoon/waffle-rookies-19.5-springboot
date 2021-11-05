package com.wafflestudio.seminar.domain.seminar.exception

import com.wafflestudio.seminar.global.common.exception.DataNotFoundException
import com.wafflestudio.seminar.global.common.exception.ErrorType

class SeminarNotFoundException(detail: String = ""):
        DataNotFoundException(ErrorType.SEMINAR_NOT_FOUND, detail)