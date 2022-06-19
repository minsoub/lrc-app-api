package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.request.StatusValueListRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.service.StatusValueListService;
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
public class StatusValueListController {

    private final StatusValueListService statusValueListService;


    /**
     * 상태값 관리 모두 가져오기
     * @return StatusValueList
     */
    @GetMapping("/status-code")
    @Operation(summary = "상태값 관리 모두 가져오기", description = "상태값 관리 목록 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getStatusValue() {
        return ResponseEntity.ok().body(statusValueListService.getStatusValue()
                .collectList()
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 상태값 관리 트리 구조 만들기
     *
     * @return StatusValueList
     */
    @GetMapping("/status-code/tree")
    @Operation(summary = "상태값 관리 트리 구조 만들기", description = "상태값 관리 트리 목록 정보를 조회합니다.")
    public ResponseEntity<Mono<SingleResponse>> getStatusValueTree() {
        return ResponseEntity.ok().body(statusValueListService.getStatusValueTree()
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 상태값 관리 1개 저장
     * @param statusValueListRequest
     * @return StatusValueListResponse
     */
    @PostMapping("/status-code")
    @Operation(summary = "상태값 관리 1개 저장", description = "상태값 관리를 저장 할때 트리 order를 수정 해야 한다. 저장합니다.")
    public ResponseEntity<Mono<?>> createStatusValue(@Parameter(name = "status Object", description = "상태값 관리 Model", in = ParameterIn.PATH)
                                                         @RequestBody StatusValueListRequest statusValueListRequest) {
        return ResponseEntity.ok().body(statusValueListService.create(statusValueListRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 상태값 관리 1개 삭제
     * @param id
     * @return StatusValueListResponse
     */
    @DeleteMapping("/statuscode/{id}")
    @Operation(summary = "상태값 관리 1개 삭제", description = "상태값 관리를 저장 할때 트리 order를 수정 해야 한다. 삭제합니다.")
    public ResponseEntity<Mono<?>> deleteStatusValue(@Parameter(name = "id", description = "상태값 관리 id", in = ParameterIn.PATH)
                                                         @PathVariable("id") String id) {
        return ResponseEntity.ok().body(statusValueListService.deleteStatusValue(id).then(
                Mono.just(new SingleResponse())
        ));
    }
}
