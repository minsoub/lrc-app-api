package com.bithumbsystems.lrc.management.api.core.exception;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.model.response.ErrorResponse;
import com.bithumbsystems.lrc.management.api.v1.audit.exception.AuditLogException;
import com.bithumbsystems.lrc.management.api.v1.exception.LrcException;
import com.bithumbsystems.lrc.management.api.v1.faq.category.exception.FaqCategoryException;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.file.exception.FileException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.exception.FoundationException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.exception.HistoryException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.exception.FoundationInfoException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.exception.IcoInfoException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.exception.MarketingQuantityException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.exception.ProjectInfoException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectlink.exception.ProjectLinkException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.exception.ReviewEstimateException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.exception.SubmittedDocumentFileException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.exception.SubmittedDocumentUrlException;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.exception.LineMngException;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.exception.StatusValueListException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> serverExceptionHandler(Exception ex) {
        log.error(ex.getMessage(), ex);
        ErrorData errorData = new ErrorData(ErrorCode.UNKNOWN_ERROR);
        return ResponseEntity.internalServerError().body(Mono.just(new ErrorResponse(errorData)));
    }

    @ExceptionHandler(FileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> fileExceptionHandler(FileException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(AuditLogException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> auditLogExceptionHandler(AuditLogException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(FaqCategoryException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> faqCategoryExceptionHandler(FaqCategoryException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(FaqContentException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> faqContentExceptionHandler(FaqContentException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(FoundationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> foundationExceptionHandler(FoundationException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(HistoryException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> historyExceptionHandler(HistoryException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(FoundationInfoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> foundationInfoExceptionHandler(FoundationException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(IcoInfoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> icoInfoExceptionHandler(IcoInfoException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(MarketingQuantityException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> MarketingQuantityExceptionHandler(MarketingQuantityException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(ProjectInfoException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> projectInfoExceptionHandler(ProjectInfoException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(ProjectLinkException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> projectLinkExceptionHandler(ProjectLinkException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(ReviewEstimateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> reviewEstimateExceptionHandler(ReviewEstimateException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(SubmittedDocumentFileException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> submittedDocumentFileExceptionHandler(SubmittedDocumentFileException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(SubmittedDocumentUrlException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> submittedDocumentUrlExceptionHandler(SubmittedDocumentUrlException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(LineMngException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> lineMngExceptionHandler(LineMngException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(StatusValueListException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> statusValueListExceptionHandler(StatusValueListException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }

    @ExceptionHandler(LrcException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Mono<?>> lrcExceptionHandler(LrcException ex) {
        log.error(ex.getMessage(), ex);
        ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
        return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
    }
}
