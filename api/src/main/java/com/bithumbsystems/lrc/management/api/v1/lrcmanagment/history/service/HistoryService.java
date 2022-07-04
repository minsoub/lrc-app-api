package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.mapper.HistoryMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.request.HistoryRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.response.HistoryResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.service.HistoryDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Comparator;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class HistoryService {

    private final HistoryDomainService historyDomainService;

    /**
     * 히스토리 모두 가져오기
     * @return HistoryResponse
     */
    public Mono<List<HistoryResponse>> getHistory(String projectId, String keyword) {
        return historyDomainService.findBySearch(projectId, keyword)
                .map(HistoryMapper.INSTANCE::historyResponse)
                .collectSortedList(Comparator.comparing(HistoryResponse::getUpdateDate))
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 히스토리 1개 저장하기
     * @return HistoryResponse
     */
    public Mono<HistoryResponse> create(HistoryRequest historyRequest){
        return historyDomainService.save(HistoryMapper.INSTANCE.historyResponseToRequest(historyRequest))
                .map(HistoryMapper.INSTANCE::historyResponse)
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }
}
