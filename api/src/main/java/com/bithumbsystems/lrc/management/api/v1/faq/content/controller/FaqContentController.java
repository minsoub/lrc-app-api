package com.bithumbsystems.lrc.management.api.v1.faq.content.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.request.FaqContentRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.request.FaqOrderRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.content.service.FaqContentService;
import com.bithumbsystems.persistence.mongodb.faq.category.model.enums.LanguageType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/faq")
@Tag(name = "FAQ 콘텐츠 관리", description = "FAQ 콘텐츠 관리 API")
public class FaqContentController {

    private final FaqContentService faqContentService;

    /**
     * 콘텐츠 모든 정보 (카테고리명 가져오기)
     * @param language
     * @return FaqContentResponse object
     */
    @GetMapping(value = "/content")
    @Operation(summary = "FAQ 관리 - 콘텐츠 관리 - FAQ 콘텐츠 모든 정보 (카테고리명 가져오기)", description = "FAQ 콘텐츠 모든 정보를 조회합니다.", tags = "사이트 운영 > FAQ 관리 > 콘텐츠 관리 > 콘텐즈 검색")
    public ResponseEntity<Mono<?>> getAllContent(LanguageType language) {
        return ResponseEntity.ok().body(faqContentService.findJoinAll(language)
                .map((faqContentFlux) -> new MultiResponse(faqContentFlux)));
    }

    /**
     * 콘텐츠 모든 정보 테스트
     *
     * @return FaqContentResponse object
     */
    @GetMapping(value = "/contenttest")
    public ResponseEntity<Mono<?>> getAllContent1111() {
        return ResponseEntity.ok().body(faqContentService.findAll()
            .collectList()
            .map((faqContentFlux) -> new MultiResponse(faqContentFlux)));
    }

    /**
     * 콘텐츠 페이징 데이터 만들기
     * @param page - 페이지
     * @param size - 페이지 사이즈
     * @return FaqContentResponse paging
     */
    @GetMapping("/content/all")
    @Operation(summary = "FAQ 관리 - 콘텐츠 관리 - FAQ 콘텐츠 페이징 데이터 만들기", description = "FAQ 콘텐츠 모든 정보를 페이징 조회합니다.", tags = "사이트 운영 > FAQ 관리 > 콘텐츠 관리 > 카테고리 페이징")
    public ResponseEntity<Mono<?>> getPagingAll(@Parameter(name = "page", description = "page 정보", in = ParameterIn.QUERY)
                                                    @RequestParam("page") int page,
                                                @Parameter(name = "size", description = "page size 정보", in = ParameterIn.QUERY)
                                                @RequestParam("size") int size){
        return ResponseEntity.ok().body(faqContentService.getPagingContent(PageRequest.of(page, size))
            .map((faqContent) -> new SingleResponse(faqContent)));
    }

    /**
     * 콘텐츠 카테고리별로 찾기
     * @param categoryCode
     * @return FaqContentResponse object
     */
    @GetMapping("/content/category/{categoryCode}")
    @Operation(summary = "FAQ 관리 - 콘텐츠 관리 - FAQ 콘텐츠 카테고리별로 찾기", description = "FAQ 콘텐츠 카테고리별로 정보를 조회합니다.", tags = "사이트 운영 > FAQ 관리 > 콘텐츠 관리 > 콘텐츠 카테고리별로 1개 검색")
    public ResponseEntity<Mono<?>> getContentCategory(@Parameter(name = "category", description = "category 정보", in = ParameterIn.PATH)
                                                          @PathVariable String categoryCode) {
        return ResponseEntity.ok().body(faqContentService.findCategoryCode(categoryCode)
                    .map(res -> new SingleResponse(res))
        );
    }

    /**
     * 콘텐츠 1개 찾기
     * @param id
     * @return FaqContentResponse object
     */

    @GetMapping("/content/{id}")
    @Operation(summary = "FAQ 관리 - 콘텐츠 관리 - FAQ 콘텐츠 1개 찾기", description = "FAQ 콘텐츠 카테고리 정보를 1개 조회합니다.", tags = "사이트 운영 > FAQ 관리 > 콘텐츠 관리 > 콘텐츠 1개 검색")
    public ResponseEntity<Mono<?>> getContent(@Parameter(name = "id", description = "id 정보", in = ParameterIn.PATH)
                                                  @PathVariable("id") String id) {
        return ResponseEntity.ok().body(
                faqContentService.findFaqById(id).map(res -> new SingleResponse(res))
        );
    }

    /**
     * 콘텐츠 1개 저장
     * @param faqContentRequest
     * @return FaqContentResponse
     */
    @PostMapping("/content")
    @Operation(summary = "FAQ 관리 - 콘텐츠 관리 - FAQ 콘텐츠 1개 저장", description = "FAQ 콘텐츠 카테고리 정보를 1개 저장합니다.", tags = "사이트 운영 > FAQ 관리 > 콘텐츠 관리 > 콘텐츠 1개 저장")
    public ResponseEntity<Mono<?>> createContent(@Parameter(name = "content Object", description = "콘텐츠 Model")
                                                     @RequestBody FaqContentRequest faqContentRequest,
                                                 @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(faqContentService.create(faqContentRequest, account).map(c ->
            new SingleResponse(c))
        );
    }

    /**
     * 콘텐츠 업데이트
     * @param faqContentRequest
     * @return FaqContentResponse
     */
    @PutMapping("/content/{id}")
    @Operation(summary = "FAQ 관리 - 콘텐츠 관리 - FAQ 콘텐츠 1개 업데이트", description = "FAQ 콘텐츠 카테고리 정보를 1개 수정합니다.", tags = "사이트 운영 > FAQ 관리 > 콘텐츠 관리 > 카테고리 수정")
    public ResponseEntity<Mono<?>> updateContent(@Parameter(name = "id", description = "id 정보", in = ParameterIn.PATH)
                                                     @PathVariable("id") String id,
                                                 @Parameter(name = "content Object", description = "콘텐츠 Model")
                                                 @RequestBody FaqContentRequest faqContentRequest,
                                                 @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(faqContentService.updateContent(id, faqContentRequest, account).map(c ->
            new SingleResponse(c))
        );
    }
    /**
     * 콘텐츠 Order 순서 저장
     * @param faqOrderRequest
     * @return FaqContentResponse
     */
    @PutMapping("/content")
    @Operation(summary = "FAQ 관리 - 콘텐츠 관리 - FAQ 콘텐츠 Order 업데이트", description = "FAQ 콘텐츠 Order 수정합니다.", tags = "사이트 운영 > FAQ 관리 > 콘텐츠 관리 > 콘텐츠 order 저장")
    public ResponseEntity<Mono<?>> updateContent(
            @Parameter(name = "Order List Object", description = "콘텐츠 Order Model")
                                                 @RequestBody FaqOrderRequest faqOrderRequest,
            @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(faqContentService.updateOrderContent(faqOrderRequest, account).map(c ->
                new SingleResponse(c))
        );
    }
    /**
     * 콘텐츠 삭제
     * @param id
     * @return FaqContentResponse
     */
    @DeleteMapping("/content/{id}")
    @Operation(summary = "FAQ 관리 - 콘텐츠 관리 - FAQ 콘텐츠 1개 삭제", description = "FAQ 콘텐츠 카테고리 정보를 1개 삭제합니다.", tags = "사이트 운영 > FAQ 관리 > 콘텐츠 관리 > 콘텐츠 삭제")
    public ResponseEntity<Mono<?>> deleteContent(@Parameter(name = "id", description = "id 정보", in = ParameterIn.PATH)
                                                     @PathVariable("id") String id,
                                                 @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(faqContentService.deleteContent(id, account).then(
            Mono.just(new SingleResponse()))
        );
    }


    /**
     * 콘텐츠 여러게 삭제
     * @param ids
     * @return FaqContentResponse
     */
    @DeleteMapping("/content")
    @Operation(summary = "FAQ 관리 - 콘텐츠 관리 - FAQ 콘텐츠 여러개 삭제", description = "FAQ 콘텐츠 카테고리 정보를 여러개 삭제합니다.", tags = "사이트 운영 > FAQ 관리 > 콘텐츠 관리 > 콘텐츠 여러게 삭제")
    public ResponseEntity<Mono<?>> deleteContent(@Parameter(name = "id list", description = "id 정보", in = ParameterIn.QUERY)
                                                     @RequestParam List<String> ids) {
        return ResponseEntity.ok().body(faqContentService.deleteContents(ids).then(
            Mono.just(new SingleResponse()))
        );
    }

    /**
     * 콘텐츠 검색
     * @param keyword
     * @return FaqContentResponse object
     */
    @GetMapping("/content/search")
    public ResponseEntity<Mono<?>> search(@RequestParam String keyword) {
        return ResponseEntity.ok().body(faqContentService.search(keyword)
            .collectList()
            .map(list -> new MultiResponse(list))
        );
    }
}
