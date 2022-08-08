package com.bithumbsystems.lrc.management.api.v1.faq.category.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.faq.category.model.request.FaqCategoryRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.category.model.response.FaqCategoryResponse;
import com.bithumbsystems.lrc.management.api.v1.faq.category.service.FaqCategoryService;
import com.bithumbsystems.persistence.mongodb.faq.category.model.enums.LanguageType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Comparator;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/faq")
@Tag(name = "카테고리 관리", description = "카테고리 관리 API")
public class FaqCategoryController {

    @Autowired
    private final FaqCategoryService faqCategoryService;

    /**
     * 카테고리 모든 정보
     * @return FaqCategoryResponse object
     */
    @GetMapping("/category")
    @Operation(summary = "FAQ 관리 - 카테고리 관리 - 카테고리 모든 정보", description = "카테고리 모든 정보를 조회합니다.", tags = "사이트 운영 > FAQ 관리 > 카테고리 관리 > 카테고리 검색")
    public ResponseEntity<Mono<?>> getAllCategory( @RequestParam(required = true) LanguageType language) {
        return ResponseEntity.ok().body(faqCategoryService.findAll(language)
                .collectSortedList(Comparator.comparing(FaqCategoryResponse::getCreateDate).reversed())
                .map(c -> new MultiResponse(c))
        );
    }

    /**
     * 카테고리 1개 찾기
     * @param id
     * @return FaqContentResponse object
     */
    @GetMapping("/category/{id}")
    @Operation(summary = "FAQ 관리 - 카테고리 관리 - 카테고리 1개 찾기", description = "카테고리 정보 1개 조회합니다.", tags = "사이트 운영 > FAQ 관리 > 카테고리 관리 > 카테고리 1개 검색")
    public ResponseEntity<Mono<?>> getCategory(@Parameter(name = "id", description = "id 정보", in = ParameterIn.PATH)
                                                   @PathVariable("id") String id) {
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
    @Operation(summary = "FAQ 관리 - 카테고리 관리 - 카테고리 1개 저장", description = "카테고리 정보 저장합니다.", tags = "사이트 운영 > FAQ 관리 > 카테고리 관리 > 카테고리 1개 저장")
    public ResponseEntity<Mono<?>> createCategory(@Parameter(name = "category Object", description = "카테고리 Model")
                                                      @RequestBody FaqCategoryRequest faqCategoryRequest,
                                                  @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(faqCategoryService.create(faqCategoryRequest, account)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 카테고리 업데이트
     * @param faqCategoryRequest
     * @return FaqCategoryResponse
     */
    @PutMapping("/category/{id}")
    @Operation(summary = "FAQ 관리 - 카테고리 관리 - 카테고리 업데이트", description = "카테고리 정보 수정합니다.", tags = "사이트 운영 > FAQ 관리 > 카테고리 관리 > 카테고리 수정")
    public ResponseEntity<Mono<?>> updateCategory(@Parameter(name = "id", description = "id 정보", in = ParameterIn.PATH)
                                                      @PathVariable("id") String id, @RequestBody FaqCategoryRequest faqCategoryRequest,
                                                  @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(faqCategoryService.updateCategory(id, faqCategoryRequest, account)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * 카테고리 삭제
     * @param code
     * @return FaqCategoryResponse
     */
    @DeleteMapping("/category/{code}")
    @Operation(summary = "FAQ 관리 - 카테고리 관리 - 카테고리 삭제", description = "카테고리 정보 삭제합니다.", tags = "사이트 운영 > FAQ 관리 > 카테고리 관리 > 카테고리 삭제")
    public ResponseEntity<Mono<?>> deleteCategory(@Parameter(name = "code", description = "code Id", in = ParameterIn.PATH)
                                                      @PathVariable("code") String code,
                                                  @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(faqCategoryService.deleteCategory(code, account)
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
    @Operation(summary = "FAQ 관리 - 카테고리 관리 - 카테고리 페이징", description = "카테고리 정보를 페이징 조회합니다.", tags = "사이트 운영 > FAQ 관리 > 카테고리 관리 > 카테고리 페이징")
    public ResponseEntity<Mono<?>> getAll(@Parameter(name = "page", description = "page 정보", in = ParameterIn.QUERY)
                                              @RequestParam("page") int page,
                                          @Parameter(name = "size", description = "page size 정보", in = ParameterIn.QUERY)
                                          @RequestParam("size") int size){
        return ResponseEntity.ok().body(faqCategoryService.getProducts(PageRequest.of(page, size))
                .map(c -> new SingleResponse(c)));
    }
}
