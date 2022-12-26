package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.service;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.model.entity.IcoInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.repository.IcoInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * The type Ico info domain service.
 */
@Service
@RequiredArgsConstructor
public class IcoInfoDomainService {

  private final IcoInfoRepository icoInfoRepository;

  /**
   * 상장정보 리턴.
   *
   * @param id the id
   * @return mono
   */
  public Mono<IcoInfo> findById(String id) {
    return icoInfoRepository.findById(id);
  }

  /**
   * 상장 정보 1개 id 찾기.
   *
   * @param projectId the project id
   * @return IcoInfoResponse Object
   */
  public Flux<IcoInfo> findByProjectId(String projectId) {
    return icoInfoRepository.findByProjectId(projectId);
  }

  /**
   * 상장 정보 여러개 저장 및 업데이트.
   *
   * @param icoInfo the ico info
   * @return IcoInfoResponse Object
   */
  public Mono<IcoInfo> save(IcoInfo icoInfo) {
    return icoInfoRepository.save(icoInfo);
  }

  /**
   * 상장 정보 신규 등록.
   *
   * @param icoInfo the ico info
   * @return mono
   */
  public Mono<IcoInfo> insert(IcoInfo icoInfo) {
    return icoInfoRepository.insert(icoInfo);
  }
}
