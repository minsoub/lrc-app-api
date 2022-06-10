package com.bithumbsystems.persistence.mongodb.statusmanagement.businesslist.service;

import com.bithumbsystems.persistence.mongodb.statusmanagement.businesslist.model.entity.BusinessList;
import com.bithumbsystems.persistence.mongodb.statusmanagement.businesslist.repository.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BusinessListDomainService {

    private final BusinessRepository businessRepository;

    /**
     * 비즈니스 모두 가져오기
     * @return BusinessList
     */
    public Flux<BusinessList> findAll() {
        return businessRepository.findAll();
    }

    /**
     * 비즈니스 1개 저장
     * @param businessList
     * @return BusinessList
     */
    public Mono<BusinessList> save(BusinessList businessList) {
        return businessRepository.insert(businessList);
    }

    /**
     * 비즈니스 업데이트
     * @param businessList
     * @return BusinessList
     */
    public Mono<BusinessList> updateBusiness(BusinessList businessList) {
        businessList.setUpdateDate(LocalDateTime.now());
        return businessRepository.save(businessList);
    }

    /**
     * 비즈니스 id 찾기
     * @param id
     * @return BusinessList
     */
    public Mono<BusinessList> findById(String id) {
        return businessRepository.findById(id);
    }

    /**
     * 비즈니스 1개 삭제
     * @param id
     * @return null
     */
    public Mono<Void> deleteBusiness(String id) {
        return businessRepository.deleteById(id);
    }

}
