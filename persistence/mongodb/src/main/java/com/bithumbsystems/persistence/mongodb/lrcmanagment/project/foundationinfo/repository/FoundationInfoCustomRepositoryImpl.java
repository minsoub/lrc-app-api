package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.model.entity.FoundationInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FoundationInfoCustomRepositoryImpl implements FoundationInfoCustomRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;


    /**
     * 재단정보 및 계약 상태 검색
     * @param contractCode
     * @return FoundationInfoResponse
     */
    public Flux<FoundationInfo> findByCustomSearchAll(String contractCode) {
        Query query = new Query();

        if(StringUtils.isNotEmpty(contractCode)) {
            query.addCriteria(Criteria.where("contract_code").is(contractCode));    //계약상태
        }

        return reactiveMongoTemplate.find(query, FoundationInfo.class);
    }

    /**
     * 재단정보 검색 하기
     * @param fromDate 이전
     * @param toDate 다음
     * @param contractCode 계약상태
     * @param progressCode 진행상태
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return FoundationInfoResponse Object
     */
    public Flux<FoundationInfo> findByCustomSearch(LocalDateTime fromDate, LocalDateTime toDate, String contractCode, String progressCode, String keyword) {

        Query query = new Query();

        query.addCriteria(Criteria.where("create_date").gte(fromDate).lte(toDate)); //날짜

        if(StringUtils.isNotEmpty(contractCode)) {
            query.addCriteria(Criteria.where("contract_code").is(contractCode));    //계약상태
        }

        if(StringUtils.isNotEmpty(progressCode)) {
            query.addCriteria(Criteria.where("progress_code").is(progressCode));    //진행상테
        }

        if(StringUtils.isNotEmpty(keyword)) {   //프로젝트명 심볼
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("project_name").regex(".*" + keyword + ".*", "i"),
                    Criteria.where("symbol").regex(".*" + keyword + ".*", "i")
            ));
        }

        return reactiveMongoTemplate.find(query, FoundationInfo.class);
    }
}
