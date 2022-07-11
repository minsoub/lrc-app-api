package com.bithumbsystems.lrc.management.api.v1.dashboard.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.dashboard.service.DashBoardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/dashboard")
@Tag(name = "거래지원 현황 대시보드")
public class DashBoardController {

    private final DashBoardService dashBoardService;


    /**
     * 상태값 모두 가져오기
     * @return DashBoardStatusCodeResponse
     */
    @GetMapping("/status-code")
    @Operation(summary = "상태값 통계 모두 가져오기", description = "상태값 통계 목록 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 현황 > 상태값 조회")
    public ResponseEntity<Mono<?>> getStatusValue() {
        return ResponseEntity.ok().body(dashBoardService.getStatusCode()
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 계열관리 통계 모두 가져오기
     * @return DashBoardLineMngResponse
     */
    @GetMapping("/line-code")
    @Operation(summary = "계열관리 통계 모두 가져오기", description = "계열관리 통계 목록 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 현황 > 계열관리 조회")
    public ResponseEntity<Mono<?>> getLineMng() {
        return ResponseEntity.ok().body(dashBoardService.getLineMng()
                .map(c -> new SingleResponse(c))
        );
    }
}
