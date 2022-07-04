package com.bithumbsystems.lrc.management.api.v1.audit.exception;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class AuditLogException extends RuntimeException {

    private final ErrorCode errorCode;

    public AuditLogException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}