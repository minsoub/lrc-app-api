package com.bithumbsystems.lrc.management.api.v1.servicelog.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.servicelog.mapper.ServiceLogMapper;
import com.bithumbsystems.lrc.management.api.v1.servicelog.model.request.ServiceLogRequest;
import com.bithumbsystems.lrc.management.api.v1.servicelog.model.response.ServiceLogResponse;
import com.bithumbsystems.persistence.mongodb.servicelog.service.ServiceLogDomainService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ServiceLogService {

    private final ServiceLogDomainService serviceLogDomainService;


    /**
     * 서비스 로그 모든 정보
     * @return ServiceLogResponse Object
     */
    public Flux<ServiceLogResponse> findAll() {
        return serviceLogDomainService.findAll()
                .map(ServiceLogMapper.INSTANCE::serviceLogResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 서비스 로그 1개 저장
     * @param serviceLogRequest
     * @return ServiceLogResponse Object
     */
    public Mono<ServiceLogResponse> create(ServiceLogRequest serviceLogRequest) {
        return serviceLogDomainService.save(ServiceLogMapper.INSTANCE.serviceLogRequestToServiceLog(serviceLogRequest))
                .map(ServiceLogMapper.INSTANCE::serviceLogResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }


}
