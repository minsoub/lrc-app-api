package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.request.HistoryRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.service.HistoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
@Tag(name = "변경 히스토리", description = "변경 히스토리 API")
public class HistoryController {

    private final HistoryService historyService;

    /**
     * 히스토리 모두 가져오기
     * @return HistoryResponse
     */
    @GetMapping("/history")
    @Operation(summary = "히스토리 모두 가져오기", description = "히스토리 로그 목록 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getHistory() {
        return ResponseEntity.ok().body(historyService.getHistory()
                .map(c -> new SingleResponse(c)));
    }


    /**
     * 히스토리 1개 저장하기
     * @return HistoryResponse
     */
    @PostMapping("/history")
    @Operation(summary = "히스토리 1개 저장하기", description = "히스토리 로그 정보 저장합니다.")
    public ResponseEntity<Mono<?>> createHistory(@RequestBody HistoryRequest historyRequest) {
        return ResponseEntity.ok().body(historyService.create(historyRequest)
                .map(c -> new SingleResponse(c)));
    }
}
