package com.bithumbsystems.lrc.management.api.v1.faq.category.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.faq.category.model.request.FaqCategoryRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.category.service.FaqCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/faq")
public class FaqCategoryController {

    @Autowired
    private final FaqCategoryService faqCategoryService;

    /**
     * 카테고리 모든 정보
     * @return FaqCategoryResponse object
     */
    @GetMapping("/category")
    public ResponseEntity<Mono<?>> getAllCategory() {
        return ResponseEntity.ok().body(faqCategoryService.findAll()
                .collectList()
                .map(c -> new MultiResponse(c))
        );
    }

    /**
     * 카테고리 1개 찾기
     * @param id
     * @return FaqContentResponse object
     */
    @GetMapping("/category/{id}")
    public ResponseEntity<Mono<?>> getCategory(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(faqCategoryService.findCategoryById(id)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 카테고리 1개 저장
     * @param faqCategoryRequest
     * @return FaqCategoryResponse
     */
    @PostMapping("/category")
    public ResponseEntity<Mono<?>> createCategory(@RequestBody FaqCategoryRequest faqCategoryRequest) {
        return ResponseEntity.ok().body(faqCategoryService.create(faqCategoryRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 카테고리 업데이트
     * @param faqCategoryRequest
     * @return FaqCategoryResponse
     */
    @PutMapping("/category/{id}")
    public ResponseEntity<Mono<?>> updateCategory(@PathVariable("id") String id, @RequestBody FaqCategoryRequest faqCategoryRequest) {
        return ResponseEntity.ok().body(faqCategoryService.updateCategory(id, faqCategoryRequest)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 카테고리 삭제
     * @param code
     * @return FaqCategoryResponse
     */
    @DeleteMapping("/category/{code}")
    public ResponseEntity<Mono<?>> deleteCategory(@PathVariable("code") String code) {
        return ResponseEntity.ok().body(faqCategoryService.deleteCategory(code)
                .then(Mono.just(new SingleResponse()))
        );
    }

    /**
     * 카테고리 페이징
     * @param page - 페이지
     * @param size - 페이지 사이즈
     * @return FaqContentResponse paging
     */
    @GetMapping("/category/all")
    public ResponseEntity<Mono<?>> getAll(@RequestParam("page") int page, @RequestParam("size") int size){
        return ResponseEntity.ok().body(faqCategoryService.getProducts(PageRequest.of(page, size))
                .map(c -> new SingleResponse(c)));
    }
}
