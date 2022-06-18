package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.model.request.MarketingQuantityRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.service.MarketingQuantityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment/project")
public class MarketingQuantityController {

    private final MarketingQuantityService marketingQuantityService;

    /**
     * 마케팅 수량 찾기
     * @param projectId
     * @return MarketingQuantityResponse Object
     */
    @GetMapping("/marketing-quantity/{projectId}")
    public ResponseEntity<Mono<?>> getMarketingQuantity(@PathVariable("projectId") String projectId) {
        return ResponseEntity.ok().body(marketingQuantityService.findByProjectId(projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 마케팅 수량 여러개 저장 및 업데이트
     * @param marketingQuantityRequest
     * @return MarketingQuantityResponse Object
     */
    @PostMapping("/marketing-quantity/{projectId}")
    public ResponseEntity<Mono<?>> createMarketingQuantity(@PathVariable("projectId") String projectId,
                                                           @RequestBody MarketingQuantityRequest marketingQuantityRequest) {
        return ResponseEntity.ok().body(marketingQuantityService.create(projectId, marketingQuantityRequest)
                .map(c -> new SingleResponse(c))
        );
    }
}
