package com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.model.entity.Foundation;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

public interface FoundationCustomRepository {

    /**
     * 재단정보 검색 하기
     * @param fromDate 이전
     * @param toDate 다음
     * @param contrectCode 계약상태
     * @param progressCode 진행상태
     * @param business 사업계열
     * @param network 네트워크계열
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return Foundation Object
     */
    public Flux<Foundation> findBySearch(LocalDateTime fromDate, LocalDateTime toDate, String contrectCode, String progressCode,
                                         List<String> business, List<String> network, String keyword);

}
