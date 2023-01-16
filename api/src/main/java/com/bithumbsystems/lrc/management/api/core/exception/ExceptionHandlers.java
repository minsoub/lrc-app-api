package com.bithumbsystems.lrc.management.api.core.exception;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.model.response.ErrorResponse;
import com.bithumbsystems.lrc.management.api.v1.audit.exception.AuditLogException;
import com.bithumbsystems.lrc.management.api.v1.dashboard.exception.DashBoardException;
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
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.exception.StatusCodeException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

/**
 * The type Exception handlers.
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlers {

  /**
   * Server exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> serverExceptionHandler(Exception ex) {
    log.error(ex.getMessage(), ex);
    ErrorData errorData = new ErrorData(ErrorCode.UNKNOWN_ERROR);
    return ResponseEntity.internalServerError().body(Mono.just(new ErrorResponse(errorData)));
  }

  /**
   * File exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(FileException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> fileExceptionHandler(FileException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Audit log exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(AuditLogException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> auditLogExceptionHandler(AuditLogException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Dashboard exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(DashBoardException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> dashBoardExceptionHandler(DashBoardException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Faq category exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(FaqCategoryException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> faqCategoryExceptionHandler(FaqCategoryException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Faq content exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(FaqContentException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> faqContentExceptionHandler(FaqContentException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Foundation exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(FoundationException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> foundationExceptionHandler(FoundationException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * History exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(HistoryException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> historyExceptionHandler(HistoryException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Foundation info exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(FoundationInfoException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> foundationInfoExceptionHandler(FoundationInfoException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Ico info exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(IcoInfoException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> icoInfoExceptionHandler(IcoInfoException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Marketing quantity exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(MarketingQuantityException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> marketingQuantityExceptionHandler(MarketingQuantityException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Project info exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(ProjectInfoException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> projectInfoExceptionHandler(ProjectInfoException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Project link exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(ProjectLinkException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> projectLinkExceptionHandler(ProjectLinkException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Review estimate exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(ReviewEstimateException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> reviewEstimateExceptionHandler(ReviewEstimateException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Submitted document file exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(SubmittedDocumentFileException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> submittedDocumentFileExceptionHandler(SubmittedDocumentFileException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Submitted document url exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(SubmittedDocumentUrlException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> submittedDocumentUrlExceptionHandler(SubmittedDocumentUrlException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Line mng exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(LineMngException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> lineMngExceptionHandler(LineMngException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Status value list exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(StatusCodeException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> statusValueListExceptionHandler(StatusCodeException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Lrc exception handler response entity.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(LrcException.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Mono<ErrorResponse>> lrcExceptionHandler(LrcException ex) {
    log.error(ex.getMessage(), ex);
    ErrorResponse errorResponse = new ErrorResponse(new ErrorData(ex.getErrorCode()));
    return ResponseEntity.badRequest().contentType(MediaType.APPLICATION_JSON).body(Mono.just(errorResponse));
  }

  /**
   * Web exchange bind exception handler response entity
   * Controller에서 @Valid 사용 시 발생하는 Exception.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(WebExchangeBindException.class)
  public ResponseEntity<Mono<ErrorResponse>> webExchangeBindExceptionHandler(WebExchangeBindException ex) {
    log.error(ex.getMessage(), ex);
    ErrorData errorData;
    try {
      StringBuilder stringBuilder = new StringBuilder();
      for (FieldError e : ex.getFieldErrors()) {
        stringBuilder.append(" ['");
        stringBuilder.append(e.getField());
        stringBuilder.append("' parameter is '");
        stringBuilder.append(e.getRejectedValue());
        stringBuilder.append("'. error:");
        stringBuilder.append(e.getDefaultMessage());
        stringBuilder.append("]");
      }
      errorData = new ErrorData(ErrorCode.FAIL_VALIDATE_PARAMETER.getCode(), stringBuilder.toString());
    } catch (Exception e) {
      errorData = new ErrorData(ErrorCode.FAIL_VALIDATE_PARAMETER);
    }
    return ResponseEntity.badRequest().body(Mono.just(new ErrorResponse(errorData)));
  }

  /**
   * Constraint violation exception handler response entity
   * Service에서 @Valid 사용 시 발생하는 Exception.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<Mono<ErrorResponse>> constraintViolationExceptionHandler(ConstraintViolationException ex) {
    log.error(ex.getMessage(), ex);
    ErrorData errorData;
    try {
      StringBuilder stringBuilder = new StringBuilder();
      for (ConstraintViolation<?> e : ex.getConstraintViolations()) {
        stringBuilder.append(" ['");
        stringBuilder.append(e.getPropertyPath().toString().substring(e.getPropertyPath().toString().lastIndexOf(".") + 1));
        stringBuilder.append("' parameter is '");
        stringBuilder.append(e.getInvalidValue() == null ? "" : e.getInvalidValue().toString());
        stringBuilder.append("'. error:");
        stringBuilder.append(e.getMessage());
        stringBuilder.append("]");
      }
      errorData = new ErrorData(ErrorCode.FAIL_VALIDATE_PARAMETER.getCode(), stringBuilder.toString());
    } catch (Exception e) {
      errorData = new ErrorData(ErrorCode.FAIL_VALIDATE_PARAMETER);
    }
    return ResponseEntity.badRequest().body(Mono.just(new ErrorResponse(errorData)));
  }

  /**
   * Request exception handler response entity.
   * request parameter json parsing 실패 시 발생.
   *
   * @param ex the ex
   * @return the response entity
   */
  @ExceptionHandler(ServerWebInputException.class)
  public ResponseEntity<Mono<ErrorResponse>> serverWebInputExceptionHandler(ServerWebInputException ex) {
    log.error(ex.getMessage(), ex);
    try {
      return ResponseEntity.badRequest().body(Mono.just(new ErrorResponse(new ErrorData(
          ErrorCode.FAIL_PARSING_PARAMETER.getCode(), ex.getCause().getMessage()
          .substring(0, ex.getCause().getMessage().indexOf(" nested exception"))))));
    } catch (Exception e) {
      return ResponseEntity.badRequest().body(Mono.just(new ErrorResponse(new ErrorData(ErrorCode.FAIL_PARSING_PARAMETER))));
    }
  }
}
