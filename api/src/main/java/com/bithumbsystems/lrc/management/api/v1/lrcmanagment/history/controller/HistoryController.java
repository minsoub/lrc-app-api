package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.request.HistoryRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.service.HistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment")
public class HistoryController {

    private final HistoryService historyService;

    /**
     * 히스토리 모두 가져오기
     * @return HistoryResponse
     */
    @GetMapping("/history")
    public ResponseEntity<Mono<?>> getHistory() {
        return ResponseEntity.ok().body(historyService.getHistory()
                .map(c -> new SingleResponse(c)));
    }


    /**
     * 히스토리 1개 저장하기
     * @return HistoryResponse
     */
    @PostMapping("/history")
    public ResponseEntity<Mono<?>> createHistory(@RequestBody HistoryRequest historyRequest) {
        return ResponseEntity.ok().body(historyService.create(historyRequest)
                .map(c -> new SingleResponse(c)));
    }
}
