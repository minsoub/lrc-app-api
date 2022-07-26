package com.bithumbsystems.lrc.management.api.v1.faq.category.service;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.category.exception.FaqCategoryException;
import com.bithumbsystems.lrc.management.api.v1.faq.category.mapper.FaqCategoryMapper;
import com.bithumbsystems.lrc.management.api.v1.faq.category.model.request.FaqCategoryRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.category.model.response.FaqCategoryResponse;
import com.bithumbsystems.persistence.mongodb.faq.category.model.entity.FaqCategory;
import com.bithumbsystems.persistence.mongodb.faq.category.model.enums.LanguageType;
import com.bithumbsystems.persistence.mongodb.faq.category.service.FaqCategoryDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqCategoryService {

    private final FaqCategoryDomainService faqCategoryDomainService;

    /**
     * 콘텐츠 모든 정보
     * @return FaqCategoryResponse ObjectList
     */
    public Flux<FaqCategoryResponse> findAll(LanguageType languageType) {
        return faqCategoryDomainService.findAll()
                .filter(f -> f.getLanguage().equals(languageType))
                .filter(f -> f.getUseYn().equals(true))
                .map(FaqCategoryMapper.INSTANCE::faqCategoryResponse);
    }

    /**
     * 카테고리 1개 찾기
     * @param id
     * @return FaqCategoryResponse Object
     */
    public Mono<FaqCategoryResponse> findCategoryById(String id) {
        return faqCategoryDomainService.findCategoryById(id)
                .map(FaqCategoryMapper.INSTANCE::faqCategoryResponse)
                .switchIfEmpty(Mono.error(new FaqCategoryException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 콘텐츠 1개 저장
     * @param faqCategoryRequest
     * @return FaqCategoryResponse Object
     */
    public Mono<FaqCategoryResponse> create(FaqCategoryRequest faqCategoryRequest, Account account) {
        return faqCategoryDomainService.save(FaqCategory.builder()
                        .id(UUID.randomUUID().toString())
                        .name(faqCategoryRequest.getName())
                        .language(faqCategoryRequest.getLanguage())
                        .order_no(faqCategoryRequest.getOrder_no())
                        .useYn(faqCategoryRequest.getUseYn())
                        .createDate(LocalDateTime.now())
                        .createAdminAccountId(account.getAccountId())
                        .build())
                .map(FaqCategoryMapper.INSTANCE::faqCategoryResponse)
                .switchIfEmpty(Mono.error(new FaqCategoryException(ErrorCode.FAIL_CREATE_CONTENT)));
    }

    /**
     * 카테고리 업데이트
     * @param faqCategoryRequest
     * @return FaqCategoryResponse Object
     */
    public Mono<FaqCategoryResponse> updateCategory(String id, FaqCategoryRequest faqCategoryRequest, Account account) {
        return faqCategoryDomainService.findCategoryById(id)
                .flatMap(c -> {
                    c.setOrder_no(faqCategoryRequest.getOrder_no());
                    c.setName(faqCategoryRequest.getName());
                    c.setUseYn(faqCategoryRequest.getUseYn());
                    c.setLanguage(faqCategoryRequest.getLanguage());
                    c.setCreateDate(LocalDateTime.now());
                    c.setCreateAdminAccountId(account.getAccountId());
                    return faqCategoryDomainService.updateCategory(c)
                            .map(FaqCategoryMapper.INSTANCE::faqCategoryResponse);
                })
                .switchIfEmpty(Mono.error(new FaqCategoryException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }

    /**
     * 카테고리 삭제
     * @param code
     * @return null
     */
    public Mono<FaqCategoryResponse> deleteCategory(String code, Account account) {
        return faqCategoryDomainService.findCategoryById(code)
                .flatMap(c -> {
                    c.setUseYn(false);
                    c.setCreateDate(LocalDateTime.now());
                    c.setCreateAdminAccountId(account.getAccountId());
                    return faqCategoryDomainService.updateCategory(c)
                            .map(FaqCategoryMapper.INSTANCE::faqCategoryResponse);
                })
                .switchIfEmpty(Mono.error(new FaqCategoryException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }

    /**
     * 페이징 데이터 만들기
     * @param pageRequest
     * @return FaqCategoryResponse Object paging
     */
    public Mono<Page<FaqCategory>> getProducts(PageRequest pageRequest) {
        return faqCategoryDomainService.findAllBy(pageRequest)
                .collectList()
                .zipWith(faqCategoryDomainService.getCount().map(c -> c))
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }
}
