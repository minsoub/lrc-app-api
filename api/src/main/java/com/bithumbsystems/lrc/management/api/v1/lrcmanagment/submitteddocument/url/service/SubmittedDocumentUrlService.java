package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.service;

import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.v1.faq.content.exception.FaqContentException;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.mapper.SubmittedDocumentUrlMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.model.request.SubmittedDocumentUrlRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.model.response.SubmittedDocumentUrlResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.service.SubmittedDocumentUrlDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubmittedDocumentUrlService {

    private final SubmittedDocumentUrlDomainService submittedDocumentUrlDomainService;

    /**
     * 제출 서류 관리 id, type 으로 url 찾기
     * @param projectId
     * @param type
     * @return SubmittedDocumentUrlResponse Object
     */
    public Mono<List<SubmittedDocumentUrlResponse>> findByProjectIdAndType(String projectId, String type) {
        return submittedDocumentUrlDomainService.findByProjectIdAndType(projectId, type)
                .map(SubmittedDocumentUrlMapper.INSTANCE::submittedDocumentUrlResponse)
                .collectList()
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 제출 서류 관리 url 저장
     *
     * @param submittedDocumentUrlRequest
     * @return SubmittedDocumentResponse Object
     */
    @Transactional
    public Mono<List<SubmittedDocumentUrlResponse>> saveAll(SubmittedDocumentUrlRequest submittedDocumentUrlRequest) {
        return submittedDocumentUrlDomainService.save(SubmittedDocumentUrlMapper.INSTANCE.requestToSubmittedDocumentUrl(submittedDocumentUrlRequest))
                .flatMap(res ->
                        {
                            return
                                    submittedDocumentUrlDomainService.findByProjectIdAndType(res.getProjectId(), res.getType())
                                            .map(SubmittedDocumentUrlMapper.INSTANCE::submittedDocumentUrlResponse)
                                            .collectList();
                        }
                )
                .switchIfEmpty(Mono.error(new FaqContentException(ErrorCode.NOT_FOUND_CONTENT)));
    }

    /**
     * 제출 서류 관리 url 저장
     * @param id
     * @return SubmittedDocumentResponse Object
     */
    public Mono<Void> deleteSubmittedDocumentUrl(String id) {
        return submittedDocumentUrlDomainService.findSubmittedDocumentUrlById(id)
                .flatMap(submittedDocumentUrlDomainService::deleteSubmittedDocumentUrl);
    }
}
