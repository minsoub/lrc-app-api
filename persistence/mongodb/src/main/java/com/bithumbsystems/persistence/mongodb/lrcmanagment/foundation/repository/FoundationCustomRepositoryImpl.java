package com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.model.entity.Foundation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FoundationCustomRepositoryImpl implements FoundationCustomRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    /**
     * 재단 검색 하기
     * @param fromDate 이전
     * @param toDate 다음
     * @param contractCode 계약상태
     * @param progressCode 진행상태
     * @param business 사업계열
     * @param network 네트워크계열
     * @param keyword 프로젝트명,심볼 조건 검색
     * @return Foundation Object
     */
    public Flux<Foundation> findBySearch(LocalDateTime fromDate, LocalDateTime toDate, String contractCode, String progressCode,
                                         List<String> business, List<String> network, String keyword)
    {
        Query query = new Query();

        query.addCriteria(Criteria.where("create_date").gte(fromDate).lte(toDate)); //날짜


        if(StringUtils.isNotEmpty(contractCode)) {
            query.addCriteria(Criteria.where("contract_code").is(contractCode));    //계약상태
        }

        if(StringUtils.isNotEmpty(progressCode)) {
            query.addCriteria(Criteria.where("progress_code").is(progressCode));    //진행상테
        }

        if(business.size() > 0) {
            query.addCriteria(Criteria.where("business_list").in(business));        //사업 계열
        }

        if(network.size() > 0) {
            query.addCriteria(Criteria.where("network_list").in(network));          //네트워크 계열
        }

        if(StringUtils.isNotEmpty(keyword)) {   //프로젝트명 심볼
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("project_name").regex(".*" + keyword + ".*", "i"),
                    Criteria.where("symbol").regex(".*" + keyword + ".*", "i")
            ));
        }

        return reactiveMongoTemplate.find(query, Foundation.class);
    }
}
