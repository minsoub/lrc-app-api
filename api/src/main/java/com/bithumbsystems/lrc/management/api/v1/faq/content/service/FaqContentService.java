package com.bithumbsystems.lrc.management.api.v1.faq.content.service;

import com.bithumbsystems.lrc.management.api.v1.faq.content.mapper.FaqContentMapper;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.request.FaqContentRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.content.model.response.FaqContentResponse;
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
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class FaqContentService {

    private final FaqContentDomainService faqDomainService;

    /**
     * 콘텐츠 모든 정보
     * @return
     */
    public Flux<FaqContentResponse> findAll() {
        return faqDomainService.findAll().map(FaqContentMapper.INSTANCE::faqContentRespone);
    }

    /**
     * 콘텐츠 1개 찾기
     * @param userId
     * @return
     */
    public Mono<FaqContentResponse> findFaqById(UUID userId) {
        return faqDomainService.findFaqById(userId).map(FaqContentMapper.INSTANCE::faqContentRespone);
    }

    /**
     * 콘텐츠 userId 찾기
     * @param userId
     * @return
     */
    public Mono<FaqContentResponse> findFaqByUserId(String userId) {
        return faqDomainService.findFaqByUserId(userId).map(FaqContentMapper.INSTANCE::faqContentRespone);
    }

    /**
     * 콘텐츠 1개 저장
     * @param faqContentRequest
     * @return FaqContentResponse
     */
    public Mono<FaqContentResponse> create(FaqContentRequest faqContentRequest) {

        return faqDomainService.save(FaqContentMapper.INSTANCE.faqRequestToFaqContent(faqContentRequest))
                .map(FaqContentMapper.INSTANCE::faqContentRespone);
    }

    /**
     * 콘텐츠 업데이트
     * @param faqContentRequest
     * @return FaqContentResponse
     */
    public Mono<FaqContentResponse> updateContent(FaqContentRequest faqContentRequest) {
        String userId = faqContentRequest.getUserId();
        return faqDomainService.findFaqByUserId(userId).flatMap(c -> {
            c.setOrder(faqContentRequest.getOrder());
            c.setTitle(faqContentRequest.getTitle());
            c.setContent(faqContentRequest.getContent());
            return faqDomainService.updateContent(c).map(FaqContentMapper.INSTANCE::faqContentRespone);
        });
    }

    /**
     * 콘텐츠 삭제
     * @param userId
     * @return FaqContentResponse
     */
    public Mono<Void> deleteContent(String userId) {
        return faqDomainService.findFaqByUserId(userId).flatMap(faqContent -> {
                return faqDomainService.deleteContent(faqContent);
        });
    }

    /**
     * 페이징 데이터 만들기
     * @param pageRequest
     * @return FaqContentResponse paging
     */
    public Mono<Page<FaqContent>> getProducts(PageRequest pageRequest){
        return faqDomainService.findAllBy(pageRequest)
                .collectList()
                .zipWith(faqDomainService.getCount().map(c -> c))
                .map(t -> new PageImpl<>(t.getT1(), pageRequest, t.getT2()));
    }
}
