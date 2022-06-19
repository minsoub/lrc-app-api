package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.model.request.MarketingQuantityRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.service.MarketingQuantityService;
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
@RequestMapping("lrc/lrcmanagment/project")
@Tag(name = "마케팅 수량 정보", description = "마케팅 수량 정보 API")
public class MarketingQuantityController {

    private final MarketingQuantityService marketingQuantityService;

    /**
     * 마케팅 수량 찾기
     * @param projectId
     * @return MarketingQuantityResponse Object
     */
    @GetMapping("/marketing-quantity/{projectId}")
    @Operation(summary = "마케징 수량 조회", description = "projectId를 이용하여 마케팅 수량 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getMarketingQuantity(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                            @PathVariable("projectId") String projectId) {
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
    @Operation(summary = "마케팅 수량 여러개 저장 및 업데이트", description = "projectId를 이용하여 마케징 수량 정보를 저장/삭제 합니다.")
    public ResponseEntity<Mono<?>> createMarketingQuantity(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                               @PathVariable("projectId") String projectId,
                                                           @RequestBody MarketingQuantityRequest marketingQuantityRequest) {
        return ResponseEntity.ok().body(marketingQuantityService.create(projectId, marketingQuantityRequest)
                .map(c -> new SingleResponse(c))
        );
    }
}
