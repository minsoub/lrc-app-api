package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository.UserInfoCustomRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserInfoDomainService {
    private final UserInfoRepository userInfoRepository;
    private final UserInfoCustomRepository userInfoCustomRepository;

    public Mono<UserInfo> findById(String id) {
        return userInfoRepository.findById(id);
    }

    /**
     * 키워드를 사용해서 거래지원 사용자를 조회한다.
     *
     * @param keyword
     * @return
     */
    public Flux<UserInfo> findBySearch(String keyword) {
        return userInfoCustomRepository.findBySearch(keyword);
    }

    /**
     * 전체 데이터 조회
     *
     * @return
     */
    public Flux<UserInfo> findAll() {
        return userInfoRepository.findAll();
    }
}
