package com.bithumbsystems.lrc.management.api.v1.faq.category.service;

import com.bithumbsystems.lrc.management.api.v1.faq.category.mapper.FaqCategoryMapper;
import com.bithumbsystems.lrc.management.api.v1.faq.category.model.request.FaqCategoryRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.category.model.response.FaqCategoryResponse;
import com.bithumbsystems.persistence.mongodb.faq.category.service.FaqCategoryDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqCategoryService {

    private final FaqCategoryDomainService faqCategoryDomainService;

    /**
     * 콘텐츠 모든 정보
     * @return
     */
    public Flux<FaqCategoryResponse> findAll() {
        return faqCategoryDomainService.findAll().map(FaqCategoryMapper.INSTANCE::faqCategoryResponse);
    }

    /**
     * 카테고리 1개 찾기
     * @param id
     * @return
     */
    public Mono<FaqCategoryResponse> findCategoryById(UUID id) {
        return faqCategoryDomainService.findCategoryById(id).map(FaqCategoryMapper.INSTANCE::faqCategoryResponse);
    }

    /**
     * 카테고리 code 찾기
     * @param code
     * @return FaqCategoryResponse
     */
    public Mono<FaqCategoryResponse> findCategoryCode(String code) {
        return faqCategoryDomainService.findCategoryByCode(code).map(FaqCategoryMapper.INSTANCE::faqCategoryResponse);
    }

    /**
     * 콘텐츠 1개 저장
     * @param faqCategoryRequest
     * @return FaqCategoryResponse
     */
    public Mono<FaqCategoryResponse> create(FaqCategoryRequest faqCategoryRequest) {
        return faqCategoryDomainService.save(FaqCategoryMapper.INSTANCE.faqCategoryRequestToFaqContent(faqCategoryRequest))
                .map(FaqCategoryMapper.INSTANCE::faqCategoryResponse);
    }

    /**
     * 카테고리 업데이트
     * @param faqCategoryRequest
     * @return FaqCategoryResponse
     */
    public Mono<FaqCategoryResponse> updateCategory(FaqCategoryRequest faqCategoryRequest) {
        String code = faqCategoryRequest.getCode();
        return faqCategoryDomainService.findCategoryByCode(code).flatMap(c -> {
            c.setOrder(faqCategoryRequest.getOrder());
            c.setCategory(faqCategoryRequest.getCategory());
            c.setUseYn(faqCategoryRequest.getUseYn());
            c.setUser(faqCategoryRequest.getUser());
            c.setLanguage(faqCategoryRequest.getLanguage());
            return faqCategoryDomainService.updateCategory(c).map(FaqCategoryMapper.INSTANCE::faqCategoryResponse);
        });
    }

    /**
     * 카테고리 삭제
     * @param code
     * @return FaqContentResponse
     */
    public Mono<Void> deleteCategory(String code) {
        return faqCategoryDomainService.findCategoryByCode(code).flatMap(faqCategory -> {
            return faqCategoryDomainService.deleteCategory(faqCategory);
        });
    }
}
