package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.model.request.SubmittedDocumentRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.service.SubmittedDocumentService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
@Tag(name = "제출 서류 관리", description = "제출 서류 관리 API")
public class SubmittedDocumentController {

    private final SubmittedDocumentService submittedDocumentService;

    /**
     * 제출 서류 목록 찾기
     * @return SubmittedDocumentResponse Object
     */
    @GetMapping("/submitted-document/type")
    @Operation(summary = "제출 서류 목록", description = "제출 서류 목록 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getSubmittedDocumentType() {
        return ResponseEntity.ok().body(Mono.just(SubmittedDocumentEnums.values())
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 제출 서류 관리 id로 찾기
     * @param projectId
     * @return SubmittedDocumentResponse Object
     */
    @GetMapping("/submitted-document/{projectId}")
    @Operation(summary = "제출 서류 관리 id로 찾기", description = "projectId를 이용하여 제출 서류 관리 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getSubmittedDocument(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                            @PathVariable("projectId") String projectId) {
        return ResponseEntity.ok().body(submittedDocumentService.findByProjectId(projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 제출 서류 관리 여러개 저장 및 업데이트 ( 이거로 사용함 )
     * 리스트로 받아서 파일 여부 확인 하여 저장 해야 함
     *
     * @param submittedDocumentRequest
     * @return SubmittedDocumentResponse Object
     */
    @PostMapping(value = "/submitted-document/upload/s3", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "제출 서류 관리 여러개 저장 및 업데이트", description = "document를 이용하여 제출 서류 관리 정보 저장 및 수정 합니다.")
    public ResponseEntity<Mono<?>> createReviewEstimateFile(@Parameter(name = "document Object", description = "제출 서류 Model", in = ParameterIn.PATH)
                                                                @ModelAttribute(value = "submittedDocumentRequest") SubmittedDocumentRequest submittedDocumentRequest) {
        return ResponseEntity.ok().body(submittedDocumentService.saveAll(Flux.just(submittedDocumentRequest))
                .map(c -> new SingleResponse(c)));
    }
}
