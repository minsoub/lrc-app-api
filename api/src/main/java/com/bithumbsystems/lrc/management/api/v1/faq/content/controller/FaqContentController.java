package com.bithumbsystems.lrc.management.api.v1.faq.content.controller;

import com.bithumbsystems.lrc.management.api.v1.faq.content.model.request.FaqContentRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.content.service.FaqContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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
    @GetMapping("/content/{userId}")
    public ResponseEntity<Mono<?>> getContent(@PathVariable("userId") String userId) {
        return ResponseEntity.ok().body(faqContentService.findFaqByUserId(userId)
            .flatMap(c ->
                faqContentService.findFaqById(c.getId())
                    .map(res -> new SingleResponse(res)))
            );
    @GetMapping("/faq_content/{id}")
    public Mono<ResponseEntity<FaqContentResponse>> getContent(@PathVariable("id") UUID id) {
        return faqContentService.findFaqById(id).map(res -> {
            return ResponseEntity.ok(res);
        }).defaultIfEmpty(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
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
     * 콘텐츠 삭제
     * @param page - 페이지
     * @param size - 페이지 사이즈
     * @return FaqContentResponse paging
     */
    @GetMapping("/faq_content/all")
    public Mono<Page<FaqContent>> getAll(@RequestParam("page") int page, @RequestParam("size") int size){
        return faqContentService.getProducts(PageRequest.of(page, size));
    }

    /**
     * 콘텐츠 다중 삭제
     * @param ids
     * @return FaqContentResponse
     */
    @DeleteMapping("/faq_content")
    public Mono<ResponseEntity<Void>> deleteContent(@RequestParam List<UUID> ids) {
        return faqContentService.deleteContents(ids).then(
                Mono.just(new ResponseEntity<Void>(HttpStatus.OK))
        ).defaultIfEmpty(
                new ResponseEntity<>(HttpStatus.NOT_FOUND)
        );
    }

    /**
     * 콘텐츠 검색
     * @param keyword
     * @return FaqContentResponse object
     */
    @GetMapping("/faq_content/search")
    public Flux<FaqContentResponse> search(@RequestParam String keyword) {
        return faqContentService.search(keyword);
    }
}
