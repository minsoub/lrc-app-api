package com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.model.request.BusinessListRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.service.BusinessListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/statusmanagment")
public class BusinessListController {

    private final BusinessListService businessListService;

    /**
     * 사업계열 모두 가져오기
     * @return BusinessListResponse
     */
    @GetMapping("/business-list")
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
    public ResponseEntity<Mono<?>> createBusiness(@RequestBody BusinessListRequest businessListRequest) {
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
    public ResponseEntity<Mono<?>> updateBusiness(@PathVariable("id") String id, @RequestBody BusinessListRequest businessListRequest) {
        return ResponseEntity.ok().body(businessListService.updateBusiness(id, businessListRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 사업계열 삭제
     * @param id
     * @return null
     */
    @DeleteMapping("/business-list/{id}")
    public ResponseEntity<Mono<?>> deleteBusiness(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(businessListService.deleteBusiness(id)
                .then(Mono.just(new SingleResponse()))
        );
    }
}
