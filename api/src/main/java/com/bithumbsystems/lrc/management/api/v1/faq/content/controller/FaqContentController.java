package com.bithumbsystems.lrc.management.api.v1.faq.content.controller;

import com.bithumbsystems.lrc.management.api.v1.faq.content.model.request.FaqContentRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.response.FaqContentResponse;
import com.bithumbsystems.lrc.management.api.v1.faq.content.service.FaqContentService;
import com.bithumbsystems.persistence.mongodb.faq.content.model.entity.FaqContent;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc")
public class FaqContentController {

    @Autowired
    private final FaqContentService faqContentService;

    /**
     * 콘텐츠 모든 정보
     * @return FaqContentResponse object
     */
    @GetMapping(value = "/faq_content")
    public Flux<FaqContentResponse> getAllContent() {
        return faqContentService.findAll();
    }

    /**
     * 콘텐츠 1개 찾기
     * @param userId
     * @return FaqContentResponse object
     */
    @GetMapping("/faq_content/{userId}")
    public Mono<ResponseEntity<FaqContentResponse>> getContent(@PathVariable("userId") String userId) {

        return faqContentService.findFaqByUserId(userId).flatMap(c -> {
            return faqContentService.findFaqById(c.getId()).map(res -> {
                return ResponseEntity.ok(res);
            }).defaultIfEmpty(
                    new ResponseEntity<>(HttpStatus.NOT_FOUND)
            );
        });
    }

    /**
     * 콘텐츠 1개 저장
     * @param faqContentRequest
     * @return FaqContentResponse
     */
    @PostMapping("/faq_content")
    public Mono<ResponseEntity<FaqContentResponse>> createContent(@RequestBody FaqContentRequest faqContentRequest) {
        return faqContentService.create(faqContentRequest).map(c -> {
            return new ResponseEntity<>(c, HttpStatus.CREATED);
        }).defaultIfEmpty(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    /**
     * 콘텐츠 업데이트
     * @param faqContentRequest
     * @return FaqContentResponse
     */
    @PutMapping("/faq_content")
    public Mono<ResponseEntity<FaqContentResponse>> updateContent(@RequestBody FaqContentRequest faqContentRequest) {
        return faqContentService.updateContent(faqContentRequest).map(c -> {
            return ResponseEntity.ok(c);
        }).defaultIfEmpty(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    /**
     * 콘텐츠 삭제
     * @param userId
     * @return FaqContentResponse
     */
    @DeleteMapping("/faq_content/{userId}")
    public Mono<ResponseEntity<Void>> deleteContent(@PathVariable("userId") String userId) {
        return faqContentService.deleteContent(userId).then(
                Mono.just(new ResponseEntity<Void>(HttpStatus.OK))
        ).defaultIfEmpty(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    /**
     * 콘텐츠 삭제
     * @param page - 페이지
     * @param size - 페이지 사이즈
     * @return FaqContentResponse paging
     */
    @GetMapping("/faq_content/all")
    public Mono<Page<FaqContent>> getAll(@RequestParam("page") int page, @RequestParam("size") int size){
        return faqContentService.getProducts(PageRequest.of(page, size));
    }
}
