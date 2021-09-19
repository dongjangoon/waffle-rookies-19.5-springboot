package com.wafflestudio.seminar.domain.seminar.exception

import com.wafflestudio.seminar.global.common.exception.ErrorType
import com.wafflestudio.seminar.global.common.exception.InvalidRequestException

class AlreadyEnteredException(detail: String = ""):
        InvalidRequestException(ErrorType.ALREADY_ENTER, detail)