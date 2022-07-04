package com.bithumbsystems.persistence.mongodb.lrcmanagment.history.repository;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.entity.ProjectInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Query.query;

@Slf4j
@Repository
@RequiredArgsConstructor
public class HistoryCustomRepositoryImpl implements HistoryCustomRepository {

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    /**
     * 변경 히스토리 조회
     *
     * @param projectId
     * @param keyword
     * @return
     */
    public Flux<History> findBySearch(String projectId, String keyword) {
        Query query = new Query();
        var condition = new Query();

        condition = query(new Criteria()
                .andOperator(
                        where("project_id").is(projectId)
                )
                .orOperator(
                        where("menu").regex(keyword),
                        where("subject").regex(keyword),
                        where("task_history").regex(keyword)
                )
        );

        return reactiveMongoTemplate.find(condition,  History.class);
    }
}
