package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.model.request.SubmittedDocumentRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.service.SubmittedDocumentService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment")
public class SubmittedDocumentController {

    private final SubmittedDocumentService submittedDocumentService;

    @GetMapping("/submitted-document/type")
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
    public ResponseEntity<Mono<?>> getSubmittedDocument(@PathVariable("projectId") String projectId) {
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
    public ResponseEntity<Mono<?>> createReviewEstimateFile(@ModelAttribute(value = "submittedDocumentRequest") SubmittedDocumentRequest submittedDocumentRequest) {
        return ResponseEntity.ok().body(submittedDocumentService.saveAll(Flux.just(submittedDocumentRequest))
                .map(c -> new SingleResponse(c)));
    }
}
