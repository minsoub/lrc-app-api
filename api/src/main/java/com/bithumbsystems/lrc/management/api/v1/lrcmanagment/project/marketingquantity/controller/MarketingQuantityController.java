package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
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
    @Operation(summary = "거래지원 관리 - 마케징 수량 조회", description = "projectId를 이용하여 마케팅 수량 정보를 조회합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 마케팅 수량 > 1개 검색")
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
    @Operation(summary = "거래지원 관리 - 마케팅 수량 여러개 저장 및 업데이트", description = "projectId를 이용하여 마케징 수량 정보를 저장/삭제 합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 마케팅 수량 > 저장 및 수정")
    public ResponseEntity<Mono<?>> createMarketingQuantity(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH)
                                                               @PathVariable("projectId") String projectId,
                                                           @RequestBody MarketingQuantityRequest marketingQuantityRequest,
                                                           @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(marketingQuantityService.create(projectId, marketingQuantityRequest, account)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 마케팅 수량 삭제
     * @param projectId
     * @return MarketingQuantityResponse Object
     */
    @DeleteMapping("/marketing-quantity/{projectId}/{id}")
    @Operation(summary = "거래지원 관리 - 마케징 수량 삭제", description = "projectId를 이용하여 마케팅 수량 정보를 삭제합니다.", tags = "사이트 운영 > 거래지원 관리 > 프로젝트 관리 > 마케팅 수량 > 삭제")
    public ResponseEntity<Mono<?>> deleteMarketingQuantity(@Parameter(name = "projectId", description = "project 의 projectId", in = ParameterIn.PATH) @PathVariable("projectId") String projectId,
                                                           @Parameter(name = "id", description = "마케팅 수량의 ID", in = ParameterIn.PATH) @PathVariable("id") String id,
                                                        @Parameter(hidden = true) @CurrentUser Account account){
        return ResponseEntity.ok().body(marketingQuantityService.deleteById(projectId, id, account)
                .map(c -> new SingleResponse(c))
        );
    }
}
