package com.bithumbsystems.lrc.management.api.v1.faq.content.controller;

import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.request.FaqContentRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.content.service.FaqContentService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/faq")
public class FaqContentController {

    private final FaqContentService faqContentService;

    /**
     * 콘텐츠 모든 정보
     *
     * @return FaqContentResponse object
     */
    @GetMapping(value = "/content")
    public ResponseEntity<Mono<?>> getAllContent() {
        return ResponseEntity.ok().body(faqContentService.findAll()
            .collectList()
            .map((faqContentFlux) -> new MultiResponse(faqContentFlux)));
    }


    /**
     * 콘텐츠 삭제
     * @param page - 페이지
     * @param size - 페이지 사이즈
     * @return FaqContentResponse paging
     */
    @GetMapping("/content/all")
    public ResponseEntity<Mono<?>> getAll(@RequestParam("page") int page, @RequestParam("size") int size){
        return ResponseEntity.ok().body(faqContentService.getProducts(PageRequest.of(page, size))
            .map((faqContent) -> new SingleResponse(faqContent)));
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
    @PutMapping("/content")
    public ResponseEntity<Mono<?>> updateContent(@RequestBody FaqContentRequest faqContentRequest) {
        return ResponseEntity.ok().body(faqContentService.updateContent(faqContentRequest).map(c ->
            new SingleResponse(c))
        );
    }

    /**
     * 콘텐츠 삭제
     * @param userId
     * @return FaqContentResponse
     */
    @DeleteMapping("/content/{userId}")
    public ResponseEntity<Mono<?>> deleteContent(@PathVariable("userId") String userId) {
        return ResponseEntity.ok().body(faqContentService.deleteContent(userId).then(
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
