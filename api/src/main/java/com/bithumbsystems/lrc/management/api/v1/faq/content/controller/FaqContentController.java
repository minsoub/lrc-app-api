package com.bithumbsystems.lrc.management.api.v1.faq.content.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.request.FaqContentRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.content.service.FaqContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/faq")
public class FaqContentController {

    private final FaqContentService faqContentService;

    /**
     * 콘텐츠 모든 정보 (카테고리명 가져오기)
     *
     * @return FaqContentResponse object
     */
    @GetMapping(value = "/content")
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
    public ResponseEntity<Mono<?>> getPagingAll(@RequestParam("page") int page, @RequestParam("size") int size){
        return ResponseEntity.ok().body(faqContentService.getPagingContent(PageRequest.of(page, size))
            .map((faqContent) -> new SingleResponse(faqContent)));
    }

    /**
     * 콘텐츠 카테고리별로 찾기
     * @param categoryCode
     * @return FaqContentResponse object
     */
    @GetMapping("/content/category/{categoryCode}")
    public ResponseEntity<Mono<?>> getContentCategory(@PathVariable String categoryCode) {
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
    public ResponseEntity<Mono<?>> getContent(@PathVariable("id") String id) {
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
    public ResponseEntity<Mono<?>> createContent(@RequestBody FaqContentRequest faqContentRequest) {
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
    public ResponseEntity<Mono<?>> updateContent(@PathVariable("id") String id, @RequestBody FaqContentRequest faqContentRequest) {
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
    public ResponseEntity<Mono<?>> deleteContent(@PathVariable("id") String id) {
        return ResponseEntity.ok().body(faqContentService.deleteContent(id).then(
            Mono.just(new SingleResponse()))
        );
    }


    /**
     * 콘텐츠 다중 삭제
     * @param ids
     * @return FaqContentResponse
     */
    @DeleteMapping("/content")
    public ResponseEntity<Mono<?>> deleteContent(@RequestParam List<String> ids) {
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
