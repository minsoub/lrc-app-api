package com.bithumbsystems.persistence.mongodb.faq.category.service;

import com.bithumbsystems.persistence.mongodb.faq.category.model.entity.FaqCategory;
import com.bithumbsystems.persistence.mongodb.faq.category.repository.FaqCategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FaqCategoryDomainService {

    private final FaqCategoryRepository faqCategoryRepository;

    public Flux<FaqCategory> findAll() {
        return faqCategoryRepository.findAll();
    }

    public Mono<FaqCategory> findCategoryById(UUID categoryId) {
        return faqCategoryRepository.findById(categoryId);
    }

    public Mono<FaqCategory> findCategoryByCode(String code) {
        return faqCategoryRepository.findByCode(code);
    }

    public Mono<FaqCategory> save(FaqCategory faqCategory) {
        faqCategory.setCreateAdminAccountId("카테고리 최초사용자 (세션에서 가져와야함)"); //최초사용자 (세션에서 가져와야함)
        return faqCategoryRepository.insert(faqCategory);
    }

    public Mono<FaqCategory> updateCategory(FaqCategory faqCategory) {
        faqCategory.setUpdateDate(LocalDateTime.now());
        faqCategory.setUpdateAdminAccountId("카테고리 변경된 사용자 (세션에서 가져와야함)");   //변경된 사용자 (세션에서 가져와야함)
        return faqCategoryRepository.save(faqCategory);
    }

    public Mono<Void> deleteCategory(FaqCategory faqCategory) {
        return faqCategoryRepository.delete(faqCategory);
    }
}
