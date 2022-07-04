package com.bithumbsystems.persistence.mongodb.audit.repository;

import com.bithumbsystems.persistence.mongodb.audit.model.entity.AuditLog;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Repository
@RequiredArgsConstructor
public class AuditLogCustomRepositoryImpl implements AuditLogCustomRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @Override
    public Flux<AuditLog> findPageBySearchText(LocalDate fromDate, LocalDate toDate, String keyword, String mySiteId) {

        Query query = new Query();

        query.addCriteria(Criteria.where("create_date").gte(fromDate).lte(toDate))
                .addCriteria(Criteria.where("my_site_id").is(mySiteId));

        if (StringUtils.isNotEmpty(keyword)) {   //서비스 로그 관리 목록
            query.addCriteria(new Criteria().orOperator(
                    Criteria.where("email").regex(".*" + keyword + ".*", "i"),
                    Criteria.where("ip").regex(".*" + keyword + ".*", "i"),
                    Criteria.where("menu_name").regex(".*" + keyword + ".*", "i"),
                    Criteria.where("program_name").regex(".*" + keyword + ".*", "i"),
                    Criteria.where("uri").regex(".*" + keyword + ".*", "i"),
                    Criteria.where("parameter").regex(".*" + keyword + ".*", "i")
            ));
        }

        return reactiveMongoTemplate.find(query, AuditLog.class);
    }
}

