package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.mapper.MarketingQuantityMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.model.request.MarketingQuantityRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.model.response.MarketingQuantityResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.service.MarketingQuantityDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MarketingQuantityService {

    private final MarketingQuantityDomainService marketingQuantityDomainService;

    /**
     * 마케팅 수량 id로 찾기
     *
     * @param projectId
     * @return MarketingQuantityResponse Object
     */
    public Mono<List<MarketingQuantityResponse>> findByProjectId(String projectId) {
        return marketingQuantityDomainService.findByProjectId(projectId)
                .map(MarketingQuantityMapper.INSTANCE::marketingQuantityResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));

    }

    /**
     * 마케팅 수량 여러개 저장 및 업데이트
     * @param projectId
     * @param marketingQuantityRequest
     * @return MarketingQuantityResponse Object
     */
    public Mono<List<MarketingQuantityResponse>> create(String projectId, MarketingQuantityRequest marketingQuantityRequest) {
        return Mono.just(marketingQuantityRequest.getMarketingList())
                .flatMapMany(marketings -> Flux.fromIterable(marketings))
                .flatMap(marketing ->
                        marketingQuantityDomainService.save(MarketingQuantityMapper.INSTANCE.marketingQuantityResponseToMarketingQuantity(marketing))
                )
                .then(this.findByProjectId(projectId));
    }
}
