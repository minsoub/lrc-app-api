package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.model.request.StatusCodeRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.model.request.StatusModifyRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statuscode.service.StatusCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/statusmanagment")
@Tag(name = "상태값 관리")
public class StatusCodeController {

    private final StatusCodeService statusCodeService;


    /**
     * 상태값 관리 모두 가져오기
     * @return StatusCodeResponse
     */
    @GetMapping("/status-code")
    @Operation(summary = "상태값 관리 모두 가져오기", description = "상태값 관리 목록 정보를 조회합니다.", tags = "사이트 운영 > 상태값 관리 > 상태값 관리 > 검색")
    public ResponseEntity<Mono<?>> getStatusValue() {
        return ResponseEntity.ok().body(statusCodeService.getStatusValue()
                .collectList()
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 상태값 관리 트리 구조 만들기
     * @return StatusCodeResponse
     */
    @GetMapping("/status-code/tree")
    @Operation(summary = "상태값 관리 트리 구조 만들기", description = "상태값 관리 트리 목록 정보를 조회합니다.", tags = "사이트 운영 > 상태값 관리 > 상태값 관리 > 트리구조 만들기")
    public ResponseEntity<Mono<SingleResponse>> getStatusValueTree() {
        return ResponseEntity.ok().body(statusCodeService.getStatusValueTree()
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 상태값 관리 1개 저장
     * @param statusCodeRequest
     * @return StatusCodeResponse
     */
    @PostMapping("/status-code")
    @Operation(summary = "상태값 관리 1개 저장", description = "상태값 관리를 저장 할때 트리 order를 수정 해야 한다. 저장합니다.", tags = "사이트 운영 > 상태값 관리 > 상태값 관리 > 1개 저장")
    public ResponseEntity<Mono<?>> createStatusValue(@Parameter(name = "status Object", description = "상태값 관리 Model")
                                                         @RequestBody StatusCodeRequest statusCodeRequest,
                                                     @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(statusCodeService.create(statusCodeRequest, account)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 상태값 관리 order 수정
     * @param statusModifyRequest
     * @return StatusCodeResponse
     */
    @PutMapping("/status-code")
    @Operation(summary = "상태값 관리 oder 수정", description = "상태값 관리 수정 할때 트리 order를 수정 해야 한다.", tags = "사이트 운영 > 상태값 관리 > 상태값 관리 > order 수정")
    public ResponseEntity<Mono<?>> updateStatusValue(@Parameter(name = "status Object", description = "상태값 관리 Model", in = ParameterIn.PATH)
                                                     @RequestBody StatusModifyRequest statusModifyRequest,
                                                     @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(statusCodeService.update(statusModifyRequest, account)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 상태값 관리 1개 삭제
     * @param id
     * @return StatusCodeResponse
     */
    @DeleteMapping("/statuscode/{id}")
    @Operation(summary = "상태값 관리 1개 삭제", description = "상태값 관리를 저장 할때 트리 order를 수정 해야 한다. 삭제합니다.", tags = "사이트 운영 > 상태값 관리 > 상태값 관리 > 삭제")
    public ResponseEntity<Mono<?>> deleteStatusValue(@Parameter(name = "id", description = "상태값 관리 id", in = ParameterIn.PATH)
                                                         @PathVariable("id") String id) {
        return ResponseEntity.ok().body(statusCodeService.deleteStatusValue(id).then(
                Mono.just(new SingleResponse())
        ));
    }
}
