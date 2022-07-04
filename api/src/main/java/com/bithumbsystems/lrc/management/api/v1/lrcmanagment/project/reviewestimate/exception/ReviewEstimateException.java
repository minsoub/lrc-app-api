package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.exception;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
@Getter
public class ReviewEstimateException extends RuntimeException {

    private final ErrorCode errorCode;

    public ReviewEstimateException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}