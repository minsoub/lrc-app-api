package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.controller;


import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.request.ReviewEstimateRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.service.ReviewEstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/lrcmanagment")
public class ReviewEstimateController {

    private final ReviewEstimateService reviewEstimateService;

    /**
     * 검토 평가 id로 찾기
     * @param projectId
     * @return ReviewEstimateResponse Object
     */
    @GetMapping("/review-estimate/{projectId}")
    public ResponseEntity<Mono<?>> getReviewEstimate(@PathVariable("projectId") String projectId) {
        return ResponseEntity.ok().body(reviewEstimateService.findByProjectId(projectId)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 검토 평가 여러개 저장 및 업데이트
     * @param reviewEstimateRequest
     * @return ReviewEstimateResponse Object
     */
    @PostMapping("/review-estimate/{projectId}")
    public ResponseEntity<Mono<?>> createReviewEstimate(@PathVariable("projectId") String projectId,
                                                        @RequestBody ReviewEstimateRequest reviewEstimateRequest) {
        return ResponseEntity.ok().body(reviewEstimateService.create(projectId, reviewEstimateRequest)
                .map(c -> new SingleResponse(c))
        );
    }
}
