package com.bithumbsystems.lrc.management.api.v1.faq.category.controller;

import com.bithumbsystems.lrc.management.api.v1.faq.category.model.request.FaqCategoryRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.category.model.response.FaqCategoryResponse;
import com.bithumbsystems.lrc.management.api.v1.faq.category.service.FaqCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc")
public class FaqCategoryController {

    @Autowired
    private final FaqCategoryService faqCategoryService;

    /**
     * 카테고리 모든 정보
     * @return FaqCategoryResponse object
     */
    @GetMapping("/faq_category")
    public Flux<FaqCategoryResponse> getAllCategory() {
        return faqCategoryService.findAll();
    }

    /**
     * 카테고리 1개 찾기
     * @param code
     * @return FaqContentResponse object
     */
    @GetMapping("/faq_category/{code}")
    public Mono<ResponseEntity<FaqCategoryResponse>> getCategory(@PathVariable("code") String code) {
        return faqCategoryService.findCategoryCode(code).flatMap(c -> {
            return faqCategoryService.findCategoryById(c.getId()).map(res -> {
                return ResponseEntity.ok(res);
            }).defaultIfEmpty(
                    new ResponseEntity<>(HttpStatus.NOT_FOUND)
            );
        });
    }

    /**
     * 카테고리 1개 저장
     * @param faqCategoryRequest
     * @return FaqCategoryResponse
     */
    @PostMapping("/faq_category")
    public Mono<ResponseEntity<FaqCategoryResponse>> createCategory(@RequestBody FaqCategoryRequest faqCategoryRequest) {
        return faqCategoryService.create(faqCategoryRequest).map(c -> {
            return new ResponseEntity<>(c, HttpStatus.CREATED);
        }).defaultIfEmpty(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    /**
     * 카테고리 업데이트
     * @param faqCategoryRequest
     * @return FaqCategoryResponse
     */
    @PutMapping("/faq_category")
    public Mono<ResponseEntity<FaqCategoryResponse>> updateCategory(@RequestBody FaqCategoryRequest faqCategoryRequest) {
        return faqCategoryService.updateCategory(faqCategoryRequest).map(c -> {
            return ResponseEntity.ok(c);
        }).defaultIfEmpty(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    /**
     * 카테고리 삭제
     * @param code
     * @return FaqCategoryResponse
     */
    @DeleteMapping("/faq_category/{code}")
    public Mono<ResponseEntity<Void>> deleteCategory(@PathVariable("code") String code) {
        return faqCategoryService.deleteCategory(code).then(
                Mono.just(new ResponseEntity<Void>(HttpStatus.OK))
        ).defaultIfEmpty(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }
}