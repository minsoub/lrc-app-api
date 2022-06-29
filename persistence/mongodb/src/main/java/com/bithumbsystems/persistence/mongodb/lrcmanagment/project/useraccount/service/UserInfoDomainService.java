package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository.UserInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class UserInfoDomainService {
    private final UserInfoRepository userInfoRepository;

    public Mono<UserInfo> findById(String id) {
        return userInfoRepository.findById(id);
    }
}
