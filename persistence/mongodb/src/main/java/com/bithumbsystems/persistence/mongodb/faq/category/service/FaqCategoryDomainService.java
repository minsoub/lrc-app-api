package com.bithumbsystems.persistence.mongodb.faq.category.service;

import com.bithumbsystems.persistence.mongodb.faq.category.model.entity.FaqCategory;
import com.bithumbsystems.persistence.mongodb.faq.category.repository.FaqCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class FaqCategoryDomainService {

    private final FaqCategoryRepository faqCategoryRepository;

    /**
     * 카테고리 모든 정보
     * @return FaqCategoryResponse ObjectList
     */
    public Flux<FaqCategory> findAll() {
        return faqCategoryRepository.findAll();
    }

    /**
     * 카테고리 1개 id 찾기
     * @param categoryId
     * @return FaqCategoryResponse Object
     */
    public Mono<FaqCategory> findCategoryById(String categoryId) {
        return faqCategoryRepository.findById(categoryId);
    }

    /**
     * 카테고리 1개 저장
     * @param code
     * @return FaqCategoryResponse Object
     */
    public Mono<FaqCategory> findCategoryByCode(String code) {
        return faqCategoryRepository.findByCode(code);
    }

    /**
     * 카테고리 1개 저장
     * @param faqCategory
     * @return FaqCategoryResponse Object
     */
    public Mono<FaqCategory> save(FaqCategory faqCategory) {
        faqCategory.setCreateAdminAccountId("카테고리 최초사용자 (세션에서 가져와야함)"); //최초사용자 (세션에서 가져와야함)
        return faqCategoryRepository.insert(faqCategory);
    }

    /**
     * 카테고리 업데이트
     * @param faqCategory
     * @return FaqCategoryResponse Object
     */
    public Mono<FaqCategory> updateCategory(FaqCategory faqCategory) {
        faqCategory.setUpdateDate(LocalDateTime.now());
        faqCategory.setUpdateAdminAccountId("카테고리 변경된 사용자 (세션에서 가져와야함)");   //변경된 사용자 (세션에서 가져와야함)
        return faqCategoryRepository.save(faqCategory);
    }

    /**
     * 카테고리 삭제
     * @param faqCategory
     * @return null
     */
    public Mono<Void> deleteCategory(FaqCategory faqCategory) {
        return faqCategoryRepository.delete(faqCategory);
    }

    /**
     * 페이징 데이터 만들기
     * @param pageRequest
     * @return FaqCategoryResponse ObjectList
     */
    public Flux<FaqCategory> findAllBy(PageRequest pageRequest) {
        return faqCategoryRepository.findAllBy(pageRequest);
    }

    /**
     * 모든 데이터 count
     * @return all count
     */
    public Mono<Long> getCount() {
        return faqCategoryRepository.count();
    }
}
