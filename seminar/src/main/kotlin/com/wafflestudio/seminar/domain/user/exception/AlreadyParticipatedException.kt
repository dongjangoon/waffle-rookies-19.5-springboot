package com.wafflestudio.seminar.domain.user.exception

import com.wafflestudio.seminar.global.common.exception.ConflictException
import com.wafflestudio.seminar.global.common.exception.ErrorType

class AlreadyParticipatedException(detail: String = "") :
        ConflictException(ErrorType.ALREADY_PARTICIPANT, detail)