package com.wafflestudio.seminar.domain.seminar.exception

import com.wafflestudio.seminar.global.common.exception.ErrorType
import com.wafflestudio.seminar.global.common.exception.InvalidRequestException

class InvalidOnlineRequestException(detail: String = ""):
        InvalidRequestException(ErrorType.INVALID_ONLINE_REQUEST, detail)