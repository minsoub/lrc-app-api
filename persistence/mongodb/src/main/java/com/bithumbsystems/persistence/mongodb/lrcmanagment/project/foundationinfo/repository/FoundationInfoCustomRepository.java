package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.model.entity.FoundationInfo;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Repository
public interface FoundationInfoCustomRepository {

    /**
     * 재단정보 및 계약 상태 검색
     * @param contractCode
     * @return FoundationInfoResponse
     */
    public Flux<FoundationInfo> findByCustomSearchAll(String contractCode);

    /**
     * 재단정보 검색 하기
     * @param fromDate 이전
     * @param toDate 다음
     * @param contractCode 계약상태
     * @param progressCode 진행상태
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return FoundationInfoResponse Object
     */
    public Flux<FoundationInfo> findByCustomSearch(LocalDateTime fromDate, LocalDateTime toDate, String contractCode, String progressCode, String keyword);

}
