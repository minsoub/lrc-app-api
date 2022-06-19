package com.bithumbsystems.lrc.management.api.v1.faq.content.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.request.FaqContentRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.content.service.FaqContentService;
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
     *
     * @return FaqContentResponse object
     */
    @GetMapping(value = "/content")
    @Operation(summary = "FAQ 콘텐츠 모든 정보 (카테고리명 가져오기)", description = "FAQ 콘텐츠 모든 정보를 조회합니다.")
    public ResponseEntity<Mono<?>> getAllContent() {
        return ResponseEntity.ok().body(faqContentService.findJoinAll()
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
    @Operation(summary = "FAQ 콘텐츠 페이징 데이터 만들기", description = "FAQ 콘텐츠 모든 정보를 페이징 조회합니다.")
    public ResponseEntity<Mono<?>> getPagingAll(@Parameter(name = "page", description = "page 정보", in = ParameterIn.PATH)
                                                    @RequestParam("page") int page,
                                                @Parameter(name = "size", description = "page size 정보", in = ParameterIn.PATH)
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
    @Operation(summary = "FAQ 콘텐츠 카테고리별로 찾기", description = "FAQ 콘텐츠 카테고리별로 정보를 조회합니다.")
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
    @Operation(summary = "FAQ 콘텐츠 1개 찾기", description = "FAQ 콘텐츠 카테고리 정보를 1개 조회합니다.")
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
    @Operation(summary = "FAQ 콘텐츠 1개 저장", description = "FAQ 콘텐츠 카테고리 정보를 1개 저장합니다.")
    public ResponseEntity<Mono<?>> createContent(@Parameter(name = "content Object", description = "콘텐츠 Model", in = ParameterIn.PATH)
                                                     @RequestBody FaqContentRequest faqContentRequest) {
        return ResponseEntity.ok().body(faqContentService.create(faqContentRequest).map(c ->
            new SingleResponse(c))
        );
    }

    /**
     * 콘텐츠 업데이트
     * @param faqContentRequest
     * @return FaqContentResponse
     */
    @PutMapping("/content/{id}")
    @Operation(summary = "FAQ 콘텐츠 1개 업데이트", description = "FAQ 콘텐츠 카테고리 정보를 1개 수정합니다.")
    public ResponseEntity<Mono<?>> updateContent(@Parameter(name = "id", description = "id 정보", in = ParameterIn.PATH)
                                                     @PathVariable("id") String id,
                                                 @Parameter(name = "content Object", description = "콘텐츠 Model", in = ParameterIn.PATH)
                                                 @RequestBody FaqContentRequest faqContentRequest) {
        return ResponseEntity.ok().body(faqContentService.updateContent(id, faqContentRequest).map(c ->
            new SingleResponse(c))
        );
    }

    /**
     * 콘텐츠 삭제
     * @param id
     * @return FaqContentResponse
     */
    @DeleteMapping("/content/{id}")
    @Operation(summary = "FAQ 콘텐츠 1개 삭제", description = "FAQ 콘텐츠 카테고리 정보를 1개 삭제합니다.")
    public ResponseEntity<Mono<?>> deleteContent(@Parameter(name = "id", description = "id 정보", in = ParameterIn.PATH)
                                                     @PathVariable("id") String id) {
        return ResponseEntity.ok().body(faqContentService.deleteContent(id).then(
            Mono.just(new SingleResponse()))
        );
    }


    /**
     * 콘텐츠 야라게 삭제
     * @param ids
     * @return FaqContentResponse
     */
    @DeleteMapping("/content")
    @Operation(summary = "FAQ 콘텐츠 여러개 삭제", description = "FAQ 콘텐츠 카테고리 정보를 여러개 삭제합니다.")
    public ResponseEntity<Mono<?>> deleteContent(@Parameter(name = "id list", description = "id 정보", in = ParameterIn.PATH)
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
