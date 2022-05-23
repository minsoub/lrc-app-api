package com.bithumbsystems.persistence.mongodb.faq.content.service;

import com.bithumbsystems.persistence.mongodb.faq.content.model.entity.FaqContent;
import com.bithumbsystems.persistence.mongodb.faq.content.repository.FaqContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FaqContentDomainService {

    private final FaqContentRepository faqContentRepository;

    /**
     * 콘텐츠 모든 정보
     * @return
     */
    public Flux<FaqContent> findAll() {
        return faqContentRepository.findAll();
    }

    /**
     * 콘텐츠 1개 찾기
     * @param contentId
     * @return
     */
    public Mono<FaqContent> findFaqById(UUID contentId) {
        return faqContentRepository.findById(contentId);
    }

    /**
     * 콘텐츠 userId 찾기
     * @param userId
     * @return
     */
    public Mono<FaqContent> findFaqByUserId(String userId) {
        return faqContentRepository.findByUserId(userId);
    }

    /**
     * 콘텐츠 1개 저장
     * @param faqContent
     * @return FaqContentResponse
     */
    public Mono<FaqContent> save(FaqContent faqContent) {
        faqContent.setCreateAdminAccountId("최초사용자 (세션에서 가져와야함)"); //최초사용자 (세션에서 가져와야함)
        return faqContentRepository.insert(faqContent);
    }

    /**
     * 콘텐츠 업데이트
     * @param faqContent
     * @return FaqContentResponse
     */
    public Mono<FaqContent> updateContent(FaqContent faqContent) {
        faqContent.setUpdateDate(LocalDateTime.now());
        faqContent.setUpdateAdminAccountId("변경된 사용자 (세션에서 가져와야함)");   //변경된 사용자 (세션에서 가져와야함)
        return faqContentRepository.save(faqContent);
    }

    /**
     * 콘텐츠 삭제
     * @param faqContent
     * @return FaqContentResponse
     */
    public Mono<Void> deleteContent(FaqContent faqContent) {
        return faqContentRepository.delete(faqContent);
    }

    /**
     * 페이징 데이터 만들기
     * @param pageRequest
     * @return FaqContentResponse
     */
    public Flux<FaqContent> findAllBy(PageRequest pageRequest) {
        return faqContentRepository.findAllBy(pageRequest);
    }

    /**
     * 모든 데이터 count
     * @return all count
     */
    public Mono<Long> getCount() {
        return faqContentRepository.count();
    }




}
