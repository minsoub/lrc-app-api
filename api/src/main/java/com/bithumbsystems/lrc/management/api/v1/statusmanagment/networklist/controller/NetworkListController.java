package com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.model.request.NetworkListRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.service.NetworkListService;
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
@RequestMapping("lrc/statusmanagment")
@Tag(name = "네트워크 계열")
public class NetworkListController {

    private final NetworkListService networkListService;
    /**
     * 네트워크 계열 모두 가져오기
     * @return NetworkListResponse
     */
    @GetMapping("/network-list")
    @Operation(summary = "네트워크 계열 모두 가져오기", description = "네트워크 계열 목록 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getNetwork() {
        return ResponseEntity.ok().body(networkListService.getNetwork()
                .collectList()
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 네트워크 계열 1개 저장
     * @param networkListRequest
     * @return NetworkListResponse
     */
    @PostMapping("/network-list")
    @Operation(summary = "네트워크 계열 1개 저장", description = "네트워크 계열 정보를 저장합니다.")
    public ResponseEntity<Mono<?>> createNetwork(@Parameter(name = "network Object", description = "네트워크 계열 Model", in = ParameterIn.PATH)
                                                     @RequestBody NetworkListRequest networkListRequest) {
        return ResponseEntity.ok().body(networkListService.create(networkListRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 네트워크 계열 업데이트
     * @param id
     * @param networkListRequest
     * @return NetworkListResponse
     */
    @PutMapping("/network-list/{id}")
    @Operation(summary = "네트워크 계열 업데이트", description = "네트워크 계열 정보를 수정합니다.")
    public ResponseEntity<Mono<?>> updateNetwork(@Parameter(name = "id", description = "네트워크 계열 id", in = ParameterIn.PATH)
                                                     @PathVariable("id") String id,
                                                 @Parameter(name = "network Object", description = "네트워크 계열 Model", in = ParameterIn.PATH)
                                                 @RequestBody NetworkListRequest networkListRequest) {
        return ResponseEntity.ok().body(networkListService.updateNetwork(id, networkListRequest)
                .map( c-> new SingleResponse(c)));
    }

    /**
     * 네트워크계열 삭제
     * @param id
     * @return null
     */
    @DeleteMapping("/network-list/{id}")
    @Operation(summary = "네트워크 계열 삭제", description = "네트워크 계열 정보를 삭제합니다.")
    public ResponseEntity<Mono<?>> deleteNetwork(@Parameter(name = "id", description = "네트워크 계열 id", in = ParameterIn.PATH)
                                                     @PathVariable("id") String id) {
        return ResponseEntity.ok().body(networkListService.deleteNetwork(id)
                .then(Mono.just(new SingleResponse()))
        );
    }
}
