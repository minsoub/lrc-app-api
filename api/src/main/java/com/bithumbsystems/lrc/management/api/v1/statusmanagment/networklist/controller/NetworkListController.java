package com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.model.request.NetworkListRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.service.NetworkListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/statusmanagment")
public class NetworkListController {

    private final NetworkListService networkListService;

    /**
     * 네트워크계열 모두 가져오기
     * @return NetworkListResponse
     */
    @GetMapping("/network-list")
    public ResponseEntity<Mono<?>> getNetwork() {
        return ResponseEntity.ok().body(networkListService.getNetwork()
                .collectList()
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 네트워크계열 1개 저장
     * @param networkListRequest
     * @return NetworkListResponse
     */
    @PostMapping("/network-list")
    public ResponseEntity<Mono<?>> createNetwork(@RequestBody NetworkListRequest networkListRequest) {
        return ResponseEntity.ok().body(networkListService.create(networkListRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 네트워크 업데이트
     * @param id
     * @param networkListRequest
     * @return NetworkListResponse
     */
    @PutMapping("/network-list/{id}")
    public ResponseEntity<Mono<?>> updateNetwork(@PathVariable("id") String id, @RequestBody NetworkListRequest networkListRequest) {
        return ResponseEntity.ok().body(networkListService.updateNetwork(id, networkListRequest)
                .map( c-> new SingleResponse(c)));
    }

    /**
     * 네트워크계열 삭제
     * @param id
     * @return null
     */
    @DeleteMapping("/network-list/{id}")
    public ResponseEntity<Mono<?>> deleteNetwork(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(networkListService.deleteNetwork(id)
                .then(Mono.just(new SingleResponse()))
        );
    }
}
