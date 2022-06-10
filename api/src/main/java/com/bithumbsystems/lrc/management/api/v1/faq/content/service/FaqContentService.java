package com.bithumbsystems.lrc.management.api.v1.faq.content.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.faq.content.mapper.FaqContentMapper;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.request.FaqContentRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.response.FaqContentResponse;
import com.bithumbsystems.persistence.mongodb.faq.category.service.FaqCategoryDomainService;
import com.bithumbsystems.persistence.mongodb.faq.content.model.entity.FaqContent;
import com.bithumbsystems.persistence.mongodb.faq.content.service.FaqContentDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqContentService {

    private final FaqContentDomainService faqDomainService;
    private final FaqCategoryDomainService faqCategoryDomainService;

    /**
     * 콘텐츠 모든 정보
     * @return FaqContentResponse
     */
    public Flux<FaqContentResponse> findAll() {
        return faqDomainService.findAll().map(FaqContentMapper.INSTANCE::faqContentResponse)
            .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 콘텐츠 모든 정보 (카테고리명 가져오기)
     * @return FaqContentResponse
     */
    public Mono<List<FaqContentResponse>> findJoinAll() {
        return faqDomainService.findAll()
            .flatMap(c -> Mono.just(c)
                .zipWith(faqCategoryDomainService.findCategoryByCode(c.getCategoryCode()))
                .map(m -> {
                    FaqContentResponse faqContentResponse = FaqContentMapper.INSTANCE.faqContentResponse(m.getT1());
                    faqContentResponse.setCategory(m.getT2().getCategory());
                    return faqContentResponse;
                })
                    .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT))))
                .collectSortedList(Comparator.comparing(FaqContentResponse::getOrder));
    }

    /**
     * 콘텐츠 카테고리별로 찾기
     * @param categoryCode
     * @return FaqContentResponse
     */
    public Mono<List<FaqContentResponse>> findCategoryCode(String categoryCode) {
        return faqDomainService.findFaqByCategoryCode(categoryCode)
                .flatMap(c -> Mono.just(c)
                        .zipWith(faqCategoryDomainService.findCategoryByCode(c.getCategoryCode()))
                        .map(m -> {
                            FaqContentResponse faqContentResponse = FaqContentMapper.INSTANCE.faqContentResponse(m.getT1());
                            faqContentResponse.setCategory(m.getT2().getCategory());
                            return faqContentResponse;
                        })
                        .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT))))
                .collectSortedList(Comparator.comparing(FaqContentResponse::getOrder));
    }

    /**
     * 콘텐츠 1개 찾기
     * @param userId
     * @return FaqContentResponse
     */
    public Mono<FaqContentResponse> findFaqById(String  userId) {
        return faqDomainService.findFaqById(userId).map(FaqContentMapper.INSTANCE::faqContentResponse)
            .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 콘텐츠 userId 찾기
     * @param userId
     * @return FaqContentResponse
     */
    public Mono<FaqContentResponse> findFaqByUserId(String userId) {
        return faqDomainService.findFaqByUserId(userId).map(FaqContentMapper.INSTANCE::faqContentResponse)
            .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 콘텐츠 1개 저장
     * @param faqContentRequest
     * @return FaqContentResponse
     */
    public Mono<FaqContentResponse> create(FaqContentRequest faqContentRequest) {

        return faqDomainService.save(FaqContentMapper.INSTANCE.faqRequestToFaqContent(faqContentRequest))
                .map(FaqContentMapper.INSTANCE::faqContentResponse)
            .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_CREATE_CONTENT)));
    }

    /**
     * 콘텐츠 업데이트
     * @param faqContentRequest
     * @return FaqContentResponse
     */
    public Mono<FaqContentResponse> updateContent(String id, FaqContentRequest faqContentRequest) {
        return faqDomainService.findFaqById(id).flatMap(c -> {
            c.setOrder(faqContentRequest.getOrder());
            c.setTitle(faqContentRequest.getTitle());
            c.setContent(faqContentRequest.getContent());
            return faqDomainService.updateContent(c).map(FaqContentMapper.INSTANCE::faqContentResponse);
        }).switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_UPDATE_CONTENT)));

    }

    /**
     * 콘텐츠 삭제
     * @param userId
     * @return FaqContentResponse
     */
    public Mono<Void> deleteContent(String userId) {
        return faqDomainService.findFaqById(userId).flatMap(faqDomainService::deleteContent);
    }

    /**
     * 콘텐츠 페이징 데이터 만들기
     * @param pageRequest
     * @return FaqContentResponse paging
     */
    public Mono<Page<FaqContent>> getPagingContent(PageRequest pageRequest) {
        return faqDomainService.findPagingAll(pageRequest)
                .collectList()
                .zipWith(faqDomainService.getCount().map(c -> c))
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }

    /**
     * 콘텐츠 삭제
     * @param ids
     * @return FaqContentResponse
     */
    public Flux<Void> deleteContents(List<String> ids) {
        return Flux.fromIterable(ids).flatMap((uuid) -> faqDomainService.deleteContent(uuid));
    }

    /**
     * 콘텐츠 검색
     * @return FaqContentResponse
     */
    public Flux<FaqContentResponse> search(String keyword) {
        return faqDomainService.search(keyword).map(FaqContentMapper.INSTANCE::faqContentResponse);
    }
}
