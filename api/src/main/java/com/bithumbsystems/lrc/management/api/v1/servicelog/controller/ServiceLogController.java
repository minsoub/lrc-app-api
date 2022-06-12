package com.bithumbsystems.lrc.management.api.v1.servicelog.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.servicelog.model.request.ServiceLogRequest;
import com.bithumbsystems.lrc.management.api.v1.servicelog.service.ServiceLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/")
public class ServiceLogController {

    private final ServiceLogService serviceLogService;

    /**
     * 서비스 로그 모든 정보
     * @return ServiceLogResponse object
     */
    @GetMapping("/service-log")
    public ResponseEntity<Mono<?>> getAllServiceLog() {
        return ResponseEntity.ok().body(serviceLogService.findAll()
                .collectList()
                .map(c -> new MultiResponse(c))
        );
    }

    /**
     * 서비스 로그 1개 저장
     * @param serviceLogRequest
     * @return ServiceLogResponse object
     */
    @PostMapping("/service-log")
    public ResponseEntity<Mono<?>> create(@RequestBody ServiceLogRequest serviceLogRequest) {
        return ResponseEntity.ok().body(serviceLogService.create(serviceLogRequest)
                .map(c -> new SingleResponse(c)));
    }
}
