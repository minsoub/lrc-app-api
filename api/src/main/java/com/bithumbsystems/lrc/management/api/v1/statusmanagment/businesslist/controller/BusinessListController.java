package com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.model.request.BusinessListRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.service.BusinessListService;
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
@Tag(name = "사업 계열", description = "사업 계열 API")
public class BusinessListController {

    private final BusinessListService businessListService;

    /**
     * 사업계열 모두 가져오기
     * @return BusinessListResponse
     */
    @GetMapping("/business-list")
    @Operation(summary = "사업계열 모두 가져오기", description = "사업계열 목록 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getBusiness() {
        return ResponseEntity.ok().body(businessListService.findAll()
                .collectList()
                .map(c -> new MultiResponse(c))
        );
    }


    /**
     * 사업계열 1개 저장
     * @param businessListRequest
     * @return BusinessListResponse
     */
    @PostMapping("/business-list")
    @Operation(summary = "사업계열 1개 저장", description = "사업계열 정보를 저장합니다.")
    public ResponseEntity<Mono<?>> createBusiness(@Parameter(name = "business Object", description = "사업 계열 Model", in = ParameterIn.PATH)
                                                      @RequestBody BusinessListRequest businessListRequest) {
        return ResponseEntity.ok().body(businessListService.create(businessListRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 사업계열 업데이트
     * @param id
     * @param businessListRequest
     * @return BusinessListResponse
     */
    @PutMapping("/business-list/{id}")
    @Operation(summary = "사업계열 업데이트", description = "사업계열 정보를 수정합니다.")
    public ResponseEntity<Mono<?>> updateBusiness(@Parameter(name = "id", description = "사업 계열 id", in = ParameterIn.PATH)
                                                      @PathVariable("id") String id,
                                                  @Parameter(name = "serviceLog", description = "사업 계열 Model", in = ParameterIn.PATH)
                                                  @RequestBody BusinessListRequest businessListRequest) {
        return ResponseEntity.ok().body(businessListService.updateBusiness(id, businessListRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 사업계열 삭제
     * @param id
     * @return null
     */    @DeleteMapping("/business-list/{id}")
    @Operation(summary = "사업계열 삭제", description = "사업계열 정보를 삭제합니다.")
    public ResponseEntity<Mono<?>> deleteBusiness(@Parameter(name = "id", description = "사업계열 id", in = ParameterIn.PATH)
                                                      @PathVariable("id") String id) {
        return ResponseEntity.ok().body(businessListService.deleteBusiness(id)
                .then(Mono.just(new SingleResponse()))
        );
    }
}
