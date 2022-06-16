package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.controller;


import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.request.ReviewEstimateRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.service.ReviewEstimateService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

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
/*    @PostMapping("/review-estimate/{projectId}")
    public ResponseEntity<Mono<?>> createReviewEstimate(@PathVariable("projectId") String projectId,
                                                        @RequestBody ReviewEstimateRequest reviewEstimateRequest) {
        return ResponseEntity.ok().body(reviewEstimateService.create(projectId, reviewEstimateRequest)
                .map(c -> new SingleResponse(c))
        );
    }*/

    /**
     * 검토 평가 여러개 저장 및 업데이트
     *
     * @param reviewEstimateRequest
     * @return ReviewEstimateResponse Object
     */
    /*@PostMapping(value = "/review-estimate/upload/s3", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Mono<SingleResponse>> createReviewEstimateFile(
//            @RequestPart(value = "projectId") String projectId,
            @ModelAttribute(value = "reviewEstimateRequest") Flux<ReviewEstimateRequest> reviewEstimateRequest
//                                                                         @RequestPart(value = "files", required = false) Flux<FilePart> filePart
                                                            ) {
//        return ResponseEntity.ok().body(reviewEstimateService.saveAll(projectId, filePart, reviewEstimateRequest)
//                .map(c -> new SingleResponse(c)));
        return ResponseEntity.ok().body(reviewEstimateService.saveAll(reviewEstimateRequest)
                .map(c -> new SingleResponse(c)));
    }*/


    /**
     * 검토 평가 여러개 저장 및 업데이트 ( 이거로 사용함 )
     * 리스트로 받아서 파일 여부 확인 하여 저장 해야 함
     *
     * @param reviewEstimateRequest
     * @return ReviewEstimateResponse Object
     */
    @PostMapping(value = "/review-estimate/upload/s3", consumes = MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Mono<SingleResponse>> createReviewEstimateFile(@ModelAttribute(value = "reviewEstimateRequest") ReviewEstimateRequest reviewEstimateRequest) {
        return ResponseEntity.ok().body(reviewEstimateService.saveAll(Flux.just(reviewEstimateRequest))
                .map(c -> new SingleResponse(c)));
    }
}
