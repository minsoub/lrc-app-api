package com.bithumbsystems.lrc.management.api.v1.servicelog.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.servicelog.model.request.ServiceLogRequest;
import com.bithumbsystems.lrc.management.api.v1.servicelog.service.ServiceLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/")
@Tag(name = "서비스 로그 관리", description = "서비스 로그 관리 API")
public class ServiceLogController {

    private final ServiceLogService serviceLogService;

    /**
     * 서비스 로그 모든 정보
     * @return ServiceLogResponse object
     */
    @GetMapping("/service-log")
    @Operation(summary = "서비스 로그 모든 정보", description = "서비스 로그 관리 목록 정보를 조회합니다.")
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
    @Operation(summary = "서비스 로그 1개 저장", description = "서비스 로그 관리 정보를 저장합니다.")
    public ResponseEntity<Mono<?>> create(@Parameter(name = "serviceLog", description = "서비스 로그 관리 Model", in = ParameterIn.PATH)
                                              @RequestBody ServiceLogRequest serviceLogRequest) {
        return ResponseEntity.ok().body(serviceLogService.create(serviceLogRequest)
                .map(c -> new SingleResponse(c)));
    }
}
