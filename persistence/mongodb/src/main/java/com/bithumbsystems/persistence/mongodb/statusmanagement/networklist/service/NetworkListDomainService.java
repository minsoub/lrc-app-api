package com.bithumbsystems.persistence.mongodb.statusmanagement.networklist.service;

import com.bithumbsystems.persistence.mongodb.statusmanagement.networklist.model.entity.NetworkList;
import com.bithumbsystems.persistence.mongodb.statusmanagement.networklist.repository.NetworkListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NetworkListDomainService {

    private final NetworkListRepository networkListRepository;

    /**
     * 네트워크계열 모두 가져오기
     * @return NetworkList
     */
    public Flux<NetworkList> findAll() {
        return networkListRepository.findAll();
    }

    /**
     * 네트워크계열 1개 저장
     * @param networkList
     * @return NetworkList
     */
    public Mono<NetworkList> save(NetworkList networkList) {
        return networkListRepository.insert(networkList);
    }

    /**
     * 네트워크계열 업데이트
     * @param networkList
     * @return NetworkList
     */
    public Mono<NetworkList> updateNetwork(NetworkList networkList) {
        networkList.setUpdateDate(LocalDateTime.now());
        return networkListRepository.save(networkList);
    }

    /**
     * 네트워크계열 1개 찾기
     * @param id
     * @return NetworkList
     */
    public Mono<NetworkList> findById(String id) {
        return networkListRepository.findById(id);
    }

    /**
     * 네트워크계열 1개 삭제
     * @param id
     * @return null
     */
    public Mono<Void> deleteNetwork(String id) {
        return networkListRepository.deleteById(id);
    }
}
