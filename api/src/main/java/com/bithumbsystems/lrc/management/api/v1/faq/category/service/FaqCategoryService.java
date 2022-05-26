package com.bithumbsystems.lrc.management.api.v1.faq.category.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.category.mapper.FaqCategoryMapper;
import com.bithumbsystems.lrc.management.api.v1.faq.category.model.request.FaqCategoryRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.category.model.response.FaqCategoryResponse;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.persistence.mongodb.faq.category.model.entity.FaqCategory;
import com.bithumbsystems.persistence.mongodb.faq.category.service.FaqCategoryDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqCategoryService {

    private final FaqCategoryDomainService faqCategoryDomainService;

    /**
     * 콘텐츠 모든 정보
     * @return FaqCategoryResponse ObjectList
     */
    public Flux<FaqCategoryResponse> findAll() {
        return faqCategoryDomainService.findAll()
                .map(FaqCategoryMapper.INSTANCE::faqCategoryResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 카테고리 1개 찾기
     * @param id
     * @return FaqCategoryResponse Object
     */
    public Mono<FaqCategoryResponse> findCategoryById(String id) {
        return faqCategoryDomainService.findCategoryById(id)
                .map(FaqCategoryMapper.INSTANCE::faqCategoryResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 카테고리 code 찾기
     * @param code
     * @return FaqCategoryResponse Object
     */
    public Mono<FaqCategoryResponse> findCategoryCode(String code) {
        return faqCategoryDomainService.findCategoryByCode(code)
                .map(FaqCategoryMapper.INSTANCE::faqCategoryResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 콘텐츠 1개 저장
     * @param faqCategoryRequest
     * @return FaqCategoryResponse Object
     */
    public Mono<FaqCategoryResponse> create(FaqCategoryRequest faqCategoryRequest) {
        return faqCategoryDomainService.save(FaqCategoryMapper.INSTANCE.faqCategoryRequestToFaqContent(faqCategoryRequest))
                .map(FaqCategoryMapper.INSTANCE::faqCategoryResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 카테고리 업데이트
     * @param faqCategoryRequest
     * @return FaqCategoryResponse Object
     */
    public Mono<FaqCategoryResponse> updateCategory(FaqCategoryRequest faqCategoryRequest) {
        String code = faqCategoryRequest.getCode();
        return faqCategoryDomainService.findCategoryByCode(code)
                .flatMap(c -> {
                    c.setOrder(faqCategoryRequest.getOrder());
                    c.setCategory(faqCategoryRequest.getCategory());
                    c.setUseYn(faqCategoryRequest.getUseYn());
                    c.setUser(faqCategoryRequest.getUser());
                    c.setLanguage(faqCategoryRequest.getLanguage());
                    return faqCategoryDomainService.updateCategory(c)
                            .map(FaqCategoryMapper.INSTANCE::faqCategoryResponse);
        })
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }

    /**
     * 카테고리 삭제
     * @param code
     * @return null
     */
    public Mono<Void> deleteCategory(String code) {
        return faqCategoryDomainService.findCategoryByCode(code).flatMap(faqCategory -> {
            return faqCategoryDomainService.deleteCategory(faqCategory);
        });
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
