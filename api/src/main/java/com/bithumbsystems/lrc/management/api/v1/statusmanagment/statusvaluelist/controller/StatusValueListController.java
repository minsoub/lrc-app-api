package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.request.StatusValueListRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.service.StatusValueListService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/statusmanagment")
public class StatusValueListController {

    private final StatusValueListService statusValueListService;


    /**
     * 상태값 관리 모두 가져오기
     * @return StatusValueList
     */
    @GetMapping("/status-code")
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
    public ResponseEntity<Mono<?>> createStatusValue(@RequestBody StatusValueListRequest statusValueListRequest) {
        return ResponseEntity.ok().body(statusValueListService.create(statusValueListRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 상태값 관리 1개 삭제
     * @param id
     * @return StatusValueListResponse
     */
    @DeleteMapping("/status-code/{id}")
    public ResponseEntity<Mono<?>> deleteStatusValue(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(statusValueListService.deleteStatusValue(id).then(
                Mono.just(new SingleResponse())
        ));
    }
}
