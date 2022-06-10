package com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.mapper.BusinessListMapper;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.model.request.BusinessListRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.model.response.BusinessListResponse;
import com.bithumbsystems.persistence.mongodb.statusmanagement.businesslist.service.BusinessListDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class BusinessListService {

    private final BusinessListDomainService businessListDomainService;

    /**
     * 사업계열 모든 정보
     * @return BusinessListResponse
     */
    public Flux<BusinessListResponse> findAll() {
        return businessListDomainService.findAll().map(BusinessListMapper.INSTANCE::businessListResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 사업계열 1개 저장
     * @param businessListRequest
     * @return BusinessListResponse
     */
    public Mono<BusinessListResponse> create(BusinessListRequest businessListRequest) {
        return businessListDomainService.save(BusinessListMapper.INSTANCE.businessListRequestToBusinessList(businessListRequest))
                .map(BusinessListMapper.INSTANCE::businessListResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 사업계열 업데이트
     * @param id
     * @param businessListRequest
     * @return BusinessListResponse
     */
    public Mono<BusinessListResponse> updateBusiness(String id, BusinessListRequest businessListRequest) {
        return businessListDomainService.findById(id).flatMap(c -> {
            c.setName(businessListRequest.getName());
            return businessListDomainService.updateBusiness(c)
                    .map(BusinessListMapper.INSTANCE::businessListResponse);
        })
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }

    /**
     * 사업계열 삭제
     * @param id
     * @return null
     */
    public Mono<Void> deleteBusiness(String id) {
        return businessListDomainService.findById(id).flatMap(c -> {
            return businessListDomainService.deleteBusiness(id);
        });
    }
}
