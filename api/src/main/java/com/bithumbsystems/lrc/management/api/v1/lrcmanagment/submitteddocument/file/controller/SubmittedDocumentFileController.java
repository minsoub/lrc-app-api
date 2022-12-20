package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.response.SubmittedDocumentFileResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.service.SubmittedDocumentFileService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * The type Submitted document file controller.
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
@Tag(name = "제출 서류 관리 file", description = "제출 서류 관리 API")
public class SubmittedDocumentFileController {

  private final SubmittedDocumentFileService submittedDocumentService;

  /**
   * 제출 서류 목록 찾기.
   *
   * @return SubmittedDocumentResponse Object
   */
  @GetMapping("/submitted-document/file/type")
  @Operation(summary = "거래지원 관리 - 제출 서류 목록", description = "제출 서류 목록 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 관리 > 제출 서류 관리 > 검색")
  public ResponseEntity<Mono<SingleResponse<SubmittedDocumentEnums[]>>> getSubmittedDocumentType() {
    return ResponseEntity.ok().body(Mono.just(SubmittedDocumentEnums.values()).map(SingleResponse::new)
    );
  }

  /**
   * 제출 서류 관리 Project id 으로 file 찾기.
   *
   * @param projectId the project id
   * @return SubmittedDocumentResponse Object
   */
  @GetMapping("/submitted-document/file")
  @Operation(summary = "거래지원 관리 - 제출 서류 관리 project id로 file 찾기", description = "projectId를 이용하여 제출 서류 관리 file 정보를 조회합니다.",
      tags = "사이트 운영 > 거래지원 관리 > 제출 서류 관리 > 파일 검색")
  public ResponseEntity<Mono<SingleResponse<List<SubmittedDocumentFileResponse>>>> getSubmittedDocumentFile(
      @Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.QUERY)
      @RequestParam("projectId") String projectId) {
    return ResponseEntity.ok().body(submittedDocumentService.findByProjectId(projectId).map(SingleResponse::new)
    );
  }

  /**
   * 제출 서류 관리 file 저장.
   *
   * @param submittedDocumentRequest the submitted document request
   * @param account                  the account
   * @return SubmittedDocumentResponse Object
   */
  //@PostMapping(value = "/submitted-document/file", consumes = MULTIPART_FORM_DATA_VALUE)
  //@Operation(summary = "거래지원 관리 - 제출 서류 관리 file 저장", description = "제출 서류 관리 file 정보를 저장 합니다.", tags = "사이트 운영 > 거래지원 관리 > 제출 서류 관리 > 파일 저장")
  //public ResponseEntity<Mono<SingleResponse<SubmittedDocumentFileResponse>>> createSubmittedDocumentFile(
  //    @ModelAttribute(value = "submittedDocumentRequest") SubmittedDocumentFileRequest submittedDocumentRequest,
  //    @Parameter(hidden = true) @CurrentUser Account account) {
  //  return ResponseEntity.ok().body(submittedDocumentService.saveAll(Mono.just(submittedDocumentRequest), account).map(SingleResponse::new));
  //}

  /**
   * 제출 서류 관리 file 삭제.
   *
   * @param id the id
   * @return SubmittedDocumentResponse Object
   */
  //@DeleteMapping("/submitted-document/file/{id}")
  //@Operation(summary = "거래지원 관리 - 제출 서류 관리 file 삭제", description = "제출 서류 관리 file 정보를 삭제 합니다.", tags = "사이트 운영 > 거래지원 관리 > 제출 서류 관리 > 파일 삭제")
  //public ResponseEntity<Mono<?>> deleteSubmittedDocumentFile(@Parameter(name = "id", description = "id 정보", in = ParameterIn.PATH)
  //                                                            @PathVariable("id") String id) {
  //  return ResponseEntity.ok().body(submittedDocumentService.deleteSubmittedDocumentFile(id).then(Mono.just(new SingleResponse()))
  //  );
  //}
}
