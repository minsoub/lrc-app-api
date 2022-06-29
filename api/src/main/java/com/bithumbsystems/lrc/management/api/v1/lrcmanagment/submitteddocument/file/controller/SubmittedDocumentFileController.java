package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.request.SubmittedDocumentFileRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.service.SubmittedDocumentFileService;
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
@Tag(name = "제출 서류 관리 file", description = "제출 서류 관리 API")
public class SubmittedDocumentFileController {

    private final SubmittedDocumentFileService submittedDocumentService;

    /**
     * 제출 서류 목록 찾기
     * @return SubmittedDocumentResponse Object
     */
    @GetMapping("/submitted-document/file/type")
    @Operation(summary = "제출 서류 목록", description = "제출 서류 목록 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getSubmittedDocumentType() {
        return ResponseEntity.ok().body(Mono.just(SubmittedDocumentEnums.values())
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 제출 서류 관리 Project id 으로 file 찾기
     * @param projectId
     * @return SubmittedDocumentResponse Object
     */
    @GetMapping("/submitted-document/file")
    @Operation(summary = "제출 서류 관리 project id로 file 찾기", description = "projectId를 이용하여 제출 서류 관리 file 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getSubmittedDocumentFile(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.QUERY)
                                                                @RequestParam("projectId") String projectId) {
        return ResponseEntity.ok().body(submittedDocumentService.findByProjectId(projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 제출 서류 관리 file 저장
     * @param submittedDocumentRequest
     * @return SubmittedDocumentResponse Object
     */
    @PostMapping(value = "/submitted-document/file", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "제출 서류 관리 file 저장", description = "제출 서류 관리 file 정보를 저장 합니다.")
    public ResponseEntity<Mono<?>> createSubmittedDocumentFile(@ModelAttribute(value = "submittedDocumentRequest") SubmittedDocumentFileRequest submittedDocumentRequest,
                                                               @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(submittedDocumentService.saveAll(Mono.just(submittedDocumentRequest), account)
                .map(c -> new SingleResponse(c)));
    }

    /**
     * 제출 서류 관리 file 삭제
     * @return SubmittedDocumentResponse Object
     */
    @DeleteMapping("/submitted-document/file/{id}")
    @Operation(summary = "제출 서류 관리 file 삭제", description = "제출 서류 관리 file 정보를 삭제 합니다.")
    public ResponseEntity<Mono<?>> deleteSubmittedDocumentFile(@Parameter(name = "id", description = "id 정보", in = ParameterIn.PATH)
                                                                @PathVariable("id") String id) {

        return ResponseEntity.ok().body(submittedDocumentService.deleteSubmittedDocumentFile(id).then(
                Mono.just(new SingleResponse()))
        );
    }
}
