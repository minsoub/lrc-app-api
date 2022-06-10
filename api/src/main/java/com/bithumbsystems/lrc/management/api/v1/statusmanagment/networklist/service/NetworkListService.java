package com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.mapper.NetworkListMapper;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.model.request.NetworkListRequest;
import com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.model.response.NetworkListResponse;
import com.bithumbsystems.persistence.mongodb.statusmanagement.networklist.service.NetworkListDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class NetworkListService {

    private final NetworkListDomainService networkListDomainService;

    /**
     * 네트워크계열 모두 가져오기
     * @return BusinessListResponse
     */
    public Flux<NetworkListResponse> getNetwork() {
        return networkListDomainService.findAll()
                .map(NetworkListMapper.INSTANCE::networkListResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }


    /**
     * 네트워크계열 1개 저장
     * @param networkListRequest
     * @return BusinessListResponse
     */
    public Mono<NetworkListResponse> create(NetworkListRequest networkListRequest) {
        return networkListDomainService.save(NetworkListMapper.INSTANCE.networkListRequestToNetworkList(networkListRequest))
                .map(NetworkListMapper.INSTANCE::networkListResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 네트워크계열 업데이트
     * @param id
     * @param networkListRequest
     * @return NetworkListResponse
     */
    public Mono<NetworkListResponse> updateNetwork(String id, NetworkListRequest networkListRequest) {
        return networkListDomainService.findById(id).flatMap(c -> {
            c.setName(networkListRequest.getName());
            return networkListDomainService.updateNetwork(c)
                    .map(NetworkListMapper.INSTANCE::networkListResponse);
        })
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.FAIL_UPDATE_CONTENT)));
    }

    /**
     * 네트워계열 삭제
     * @param id
     * @return null
     */
    public Mono<Void> deleteNetwork(String id) {
        return networkListDomainService.findById(id).flatMap(c -> {
            return networkListDomainService.deleteNetwork(id);
        });
    }
}
