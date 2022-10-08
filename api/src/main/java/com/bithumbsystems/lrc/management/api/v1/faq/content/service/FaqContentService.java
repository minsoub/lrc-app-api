package com.bithumbsystems.lrc.management.api.v1.faq.content.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.util.MaskingUtil;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.faq.content.mapper.FaqContentMapper;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.request.FaqContentRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.request.FaqOrderRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.response.FaqContentResponse;
import com.bithumbsystems.persistence.mongodb.faq.category.model.enums.LanguageType;
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

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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
        return faqDomainService.findAll().map(FaqContentMapper.INSTANCE::faqContentResponse);
    }

    /**
     * 콘텐츠 모든 정보 (카테고리명 가져오기)
     * @return FaqContentResponse
     */
    public Mono<List<FaqContentResponse>> findJoinAll(LanguageType languageType) {
        return faqDomainService.findAll()
                .filter(f -> f.getLanguage().equals(languageType))
                .filter(f -> f.getUseYn().equals(true))
            .flatMap(c -> Mono.just(c)
                .zipWith(faqCategoryDomainService.findCategoryById(c.getCategoryCode()))
                .map(m -> {
                    FaqContentResponse faqContentResponse = FaqContentMapper.INSTANCE.faqContentResponse(m.getT1());
                    faqContentResponse.setCategory(m.getT2().getName());
                    faqContentResponse.setEmail(MaskingUtil.getEmailMask(faqContentResponse.getEmail()));
                    return faqContentResponse;
                }))
                .collectSortedList(Comparator.comparing(FaqContentResponse::getOrderNo));
    }

    /**
     * 콘텐츠 카테고리별로 찾기
     * @param categoryCode
     * @return FaqContentResponse
     */
    public Mono<List<FaqContentResponse>> findCategoryCode(String categoryCode) {
        return faqDomainService.findFaqByCategoryCode(categoryCode)
                .flatMap(c -> Mono.just(c)
                        .zipWith(faqCategoryDomainService.findCategoryById(c.getCategoryCode()))
                        .map(m -> {
                            FaqContentResponse faqContentResponse = FaqContentMapper.INSTANCE.faqContentResponse(m.getT1());
                            faqContentResponse.setCategory(m.getT2().getName());
                            return faqContentResponse;
                        }))
                .collectSortedList(Comparator.comparing(FaqContentResponse::getOrderNo));
    }

    /**
     * 콘텐츠 1개 찾기
     * @param contentsId
     * @return FaqContentResponse
     */
    public Mono<FaqContentResponse> findFaqById(String contentsId) {
        return faqDomainService.findFaqById(contentsId).map(FaqContentMapper.INSTANCE::faqContentResponse)
            .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 콘텐츠 userId 찾기
     * @param userId
     * @return FaqContentResponse
     */
    public Mono<FaqContentResponse> findFaqByUserId(String userId) {
        return faqDomainService.findFaqByUserId(userId).map(FaqContentMapper.INSTANCE::faqContentResponse);
    }

    /**
     * 콘텐츠 1개 저장
     * @param faqContentRequest
     * @return FaqContentResponse
     */
    public Mono<FaqContentResponse> create(FaqContentRequest faqContentRequest, Account account) {

        return faqDomainService.save(FaqContent.builder()
                                .id(UUID.randomUUID().toString())
                                .categoryCode(faqContentRequest.getCategoryCode())
                                .title(faqContentRequest.getTitle())
                                .content(faqContentRequest.getContent())
                                .useYn(faqContentRequest.getUseYn())
                                .orderNo(faqContentRequest.getOrderNo())
                                .email(account.getEmail())
                                .language(faqContentRequest.getLanguage())
                                .createDate(LocalDateTime.now())
                                .createAdminAccountId(account.getAccountId())
                                .build())
                .map(FaqContentMapper.INSTANCE::faqContentResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_CREATE_CONTENT)));
    }

    /**
     * 콘텐츠 업데이트
     * @param faqContentRequest
     * @return FaqContentResponse
     */
    public Mono<FaqContentResponse> updateContent(String id, FaqContentRequest faqContentRequest, Account account) {
        return faqDomainService.findFaqById(id).flatMap(c -> {
            c.setCategoryCode(faqContentRequest.getCategoryCode());
            c.setOrderNo(faqContentRequest.getOrderNo());
            c.setTitle(faqContentRequest.getTitle());
            c.setContent(faqContentRequest.getContent());
            c.setUpdateDate(LocalDateTime.now());
            c.setUpdateAdminAccountId(account.getAccountId());
            return faqDomainService.updateContent(c).map(FaqContentMapper.INSTANCE::faqContentResponse);
        })
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }

    /**
     * 노출 순서 저장
     * @param faqOrderRequest
     * @param account
     * @return
     */
    public Mono<List<FaqContentResponse>> updateOrderContent(FaqOrderRequest faqOrderRequest, Account account) {
        return Flux.fromIterable(faqOrderRequest.getOrderList())
                .flatMap(item -> faqDomainService.findFaqById(item.getId())
                        .flatMap(result -> {
                            result.setOrderNo(item.getOrderNo());
                            result.setUpdateDate(LocalDateTime.now());
                            result.setUpdateAdminAccountId(account.getAccountId());
                            return faqDomainService.updateContent(result)
                                    .map(FaqContentMapper.INSTANCE::faqContentResponse);
                        }))
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_UPDATE_CONTENT)))
                .collectList();
    }

    /**
     * 콘텐츠 삭제
     * @param id
     * @return FaqContentResponse
     */
    public Mono<FaqContentResponse> deleteContent(String id, Account account) {
        return faqDomainService.findFaqById(id).flatMap(c -> {
            c.setUseYn(false);
            c.setUpdateDate(LocalDateTime.now());
            c.setUpdateAdminAccountId(account.getAccountId());
            return faqDomainService.updateContent(c).map(FaqContentMapper.INSTANCE::faqContentResponse);
        }).switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_UPDATE_CONTENT)));
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
        return Flux.fromIterable(ids).flatMap(faqDomainService::deleteContent);
    }

    /**
     * 콘텐츠 검색
     * @return FaqContentResponse
     */
    public Flux<FaqContentResponse> search(String keyword) {
        return faqDomainService.search(keyword).map(FaqContentMapper.INSTANCE::faqContentResponse);
    }
}
