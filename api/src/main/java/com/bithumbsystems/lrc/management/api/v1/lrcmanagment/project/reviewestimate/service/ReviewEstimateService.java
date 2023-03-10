package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.service;

import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.model.request.BucketUploadRequest;
import com.bithumbsystems.lrc.management.api.core.util.FileUtil;
import com.bithumbsystems.lrc.management.api.core.util.sender.AwsSQSSender;
import com.bithumbsystems.lrc.management.api.v1.file.exception.FileException;
import com.bithumbsystems.lrc.management.api.v1.file.service.FileService;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener.HistoryDto;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.listener.HistoryLog;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.mapper.ReviewEstimateMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.request.ReviewEstimateRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.response.ReviewEstimateResponse;
import com.bithumbsystems.persistence.mongodb.audit.model.enums.RoleType;
import com.bithumbsystems.persistence.mongodb.chat.model.enums.FileStatus;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.model.entity.ReviewEstimate;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.service.ReviewEstimateDomainService;
import java.nio.ByteBuffer;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MimeType;
import org.springframework.util.StringUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewEstimateService {

    private final ReviewEstimateDomainService reviewEstimateDomainService;
    private final HistoryLog historyLog;
    private final AwsProperties awsProperties;
    private final FileService fileService;

    private final AwsSQSSender<BucketUploadRequest> awsSQSSender;


    /**
     * ?????? ?????? project id??? ??????
     * @param projectId
     * @return ReviewEstimateResponse Object
     */
    public Mono<List<ReviewEstimateResponse>> findByProjectId(String projectId) {
        return reviewEstimateDomainService.findByProjectId(projectId)
                .map(ReviewEstimateMapper.INSTANCE::reviewEstimateResponse)
                .collectList();
    }

    /**
     * ?????? ?????? ID??? ?????? ?????? ??????
     * @param id
     * @return
     */
    public Mono<ReviewEstimateResponse> findById(String id) {
        return reviewEstimateDomainService.findById(id)
                .map(ReviewEstimateMapper.INSTANCE::reviewEstimateResponse);
    }

    /**
     * ??????????????? ?????? ?????? ????????? ??????
     * @param projectId
     * @return
     */
    public Mono<List<ReviewEstimateResponse>> findByUseData(String projectId) {
        return reviewEstimateDomainService.findByUseData(projectId)
                .map(ReviewEstimateMapper.INSTANCE::reviewEstimateResponse)
                .collectList();
    }

    /**
     * ?????? ?????? ????????? ?????? ??? ????????????
     * @param reviewEstimateRequest
     * @return ReviewEstimateResponse Object
     */
    /*public Mono<List<ReviewEstimateResponse>> create(String projectId, ReviewEstimateRequest reviewEstimateRequest) {
        return Mono.just(reviewEstimateRequest.getReviewEstimateList())
                .flatMapMany(reviewEstimates -> Flux.fromIterable(reviewEstimates))
                .flatMap(reviewEstimate ->
                        reviewEstimateDomainService.save(ReviewEstimateMapper.INSTANCE.reviewEstimateResponseToRequest(reviewEstimate))
                )
                .then(this.findByProjectId(projectId));
    }*/

    /**
     * ?????? ????????? ????????????.
     *
     * @param projectId
     * @param id
     * @param account
     * @return
     */
    @Transactional
    public Mono<ReviewEstimateResponse> deleteById(String projectId, String id, Account account) {
        return reviewEstimateDomainService.findById(id)
                .flatMap(result -> {
                    historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "?????? ??????", "", account);
                    result.setDelYn(true);
                    result.setUpdateAdminAccountId(account.getAccountId());
                    result.setUpdateDate(LocalDateTime.now());
                    return reviewEstimateDomainService.save(result)
                            .flatMap(res->Mono.just(ReviewEstimateMapper.INSTANCE.reviewEstimateResponse(res)));
                });
    }
    //@Transactional
    public Mono<List<ReviewEstimateResponse>> saveAll(ReviewEstimateRequest reviewEstimateRequest, Account account) {

        log.debug("{}", reviewEstimateRequest);
        Queue<FilePart> fileList = new LinkedList<FilePart>();
        if (reviewEstimateRequest.getFile() != null)
            fileList.addAll(reviewEstimateRequest.getFile());
        //AtomicReference<String> id = new AtomicReference<>("");
        return  Flux.fromIterable(reviewEstimateRequest.getNo())
                .flatMap(index -> {
                    String _id = null;

                    if (reviewEstimateRequest.getId().size() == 0) {
                        //id.set("");
                        _id = "";
                    } else {
                        //id.set(reviewEstimateRequest.getId().get(index));
                        _id = reviewEstimateRequest.getId().get(index);
                    }
                    String projectId = reviewEstimateRequest.getProjectId().get(index);
                    String organization = reviewEstimateRequest.getOrganization().size() == 0? "" : reviewEstimateRequest.getOrganization().get(index);
                    String result  = reviewEstimateRequest.getResult().size() == 0 ? "" : reviewEstimateRequest.getResult().get(index);
                    String reference = reviewEstimateRequest.getReference().size() == 0 ? "" : reviewEstimateRequest.getReference().get(index);
                    String fileKey = reviewEstimateRequest.getFileKey().size() == 0 ? "" : reviewEstimateRequest.getFileKey().get(index);
                    Boolean isFile = reviewEstimateRequest.getIsFile().size() == 0 ? false : reviewEstimateRequest.getIsFile().get(index);
                    String fileName = reviewEstimateRequest.getFileName().size()  == 0 ? "": reviewEstimateRequest.getFileName().get(index);

                    // file part
                    if (StringUtils.hasLength(_id)) {   // ?????? ??????
                        if(isFile) {  //???????????? ??????
                            String final_id = _id;
                            // File ????????? check.
                            String strExt = FileUtil.getFileExt(FileUtil.ALLOW_FILE_EXT_DEFAULT, fileName);
                            log.info("service file ext => {}", strExt);
                            FilePart filePart = fileList.poll();
                            MimeType contentType = filePart.headers().getContentType();

                            log.debug("content type => {}", contentType.getType());
                            // MimeType check.
                            if(Arrays.asList(FileUtil.ALLOW_MIME_TYPE_DEFAULT).contains(contentType.getType().toUpperCase()) == false){
                                return Mono.error(new FileException(ErrorCode.INVALID_FILE_EXT));
                            }

                            return DataBufferUtils.join(filePart.content()) // fileList.poll().content())
                                    .flatMap(dataBuffer -> {
                                        ByteBuffer buf = dataBuffer.asByteBuffer();
                                        String file_key = UUID.randomUUID().toString();
                                        String file_name = fileName;
                                        Long fileSize = (long) buf.array().length;
                                        log.info("byte size ===>  {}   :   {}   :   {} : ", file_key, file_name, fileSize);
                                        String strFileSize = FileUtil.getFileSize(FileUtil.ALLOW_FILE_MAX_SIZE_DEFAULT, fileSize);
                                        log.info("service upload file size => {}", strFileSize);

                                        // Mime-type??? ????????? ??????.
                                        if (FileUtil.allowContentType.containsKey(contentType.getType())) {
                                            List<String> extList = FileUtil.allowContentType.get(contentType.getType());
                                            if (!extList.contains(strExt)) {
                                                return Mono.error(new FileException(ErrorCode.INVALID_FILE_EXT));
                                            }
                                        } else {
                                            return Mono.error(new FileException(ErrorCode.INVALID_FILE_EXT));
                                        }
                                        return fileService.upload(file_key, file_name, fileSize, awsProperties.getBucket(), buf)
                                                .flatMap(res -> {
                                                    log.info("service upload res   =>       {}", res);
                                                    log.info("service upload fileName   =>       {}", fileName.toString());

                                                    return reviewEstimateDomainService.findById(final_id)
                                                            .flatMap(mode -> {
                                                                if (!mode.getOrganization().equals(organization)) {
                                                                    historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "?????? ??????", organization, account);
                                                                }
                                                                if (!mode.getResult().equals(result)) {
                                                                    historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "??????", result,  account);
                                                                }
                                                                if (StringUtils.hasLength(reference)) {
                                                                    if (!mode.getReference().equals(reference)) {
                                                                        historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "??????", reference, account);
                                                                    }
                                                                }
                                                                // ????????? ????????????.
                                                                historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "??????", Normalizer.normalize(file_name, Normalizer.Form.NFC), account);

                                                                return reviewEstimateDomainService.save(
                                                                        ReviewEstimate.builder()
                                                                                .id(final_id)
                                                                                .projectId(projectId)
                                                                                .organization(organization)
                                                                                .result(result)
                                                                                .reference(reference)
                                                                                .fileStatus(FileStatus.ING)
                                                                                .fileKey(file_key)
                                                                                .fileName(Normalizer.normalize(file_name, Normalizer.Form.NFC))
                                                                                .createDate(mode.getCreateDate())
                                                                                .createAdminAccountId(mode.getCreateAdminAccountId())
                                                                                .updateAdminAccountId(account.getAccountId())
                                                                                .updateDate(LocalDateTime.now())
                                                                                .build());
                                                            }).flatMap(r1 -> {
                                                                // ?????? ????????? ???????????? s3 ????????? ????????? ??? ????????? SQS??? ?????? ????????? ????????????.
                                                                awsSQSSender.sendMessage(makeReviewSqsData(r1), UUID.randomUUID().toString());
                                                                return Mono.just(r1);
                                                            });

                                                });
                                    });
                        } else { // ?????? ??????????????? ?????? ???????????? ??????.
                            String final_id = _id;
                            return reviewEstimateDomainService.findById(final_id)
                                            .flatMap(mode -> {
                                                if (!mode.getOrganization().equals(organization)) {
                                                    historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "??????", organization, account);
                                                }
                                                if (!mode.getResult().equals(result)) {
                                                    historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "??????", result, account);
                                                }
                                                if (StringUtils.hasLength(reference)) {
                                                    if (!mode.getReference().equals(reference)) {
                                                        historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "??????", reference, account);
                                                    }
                                                }
                                                return reviewEstimateDomainService.save(
                                                        ReviewEstimate.builder()
                                                                .id(final_id)
                                                                .projectId(projectId)
                                                                .organization(organization)
                                                                .result(result)
                                                                .reference(reference)
                                                                .fileKey(fileKey)
                                                                .fileName(fileName)
                                                                .createDate(mode.getCreateDate())
                                                                .createAdminAccountId(mode.getCreateAdminAccountId())
                                                                .updateAdminAccountId(account.getAccountId())
                                                                .updateDate(LocalDateTime.now())
                                                                .build());
                                            });
                        }
                    }else {     // ?????? ??????
                        if(isFile) {  //???????????? ??????
                            return DataBufferUtils.join(fileList.poll().content())
                                    .flatMap(dataBuffer -> {
                                        ByteBuffer buf = dataBuffer.asByteBuffer();
                                        String file_key = UUID.randomUUID().toString();
                                        String file_name = fileName;
                                        Long fileSize = (long) buf.array().length;
                                        log.info("byte size ===>  {}   :   {}   :   {} : ", file_key, file_name, fileSize);
                                        String strExt = FileUtil.getFileExt(FileUtil.ALLOW_FILE_EXT_DEFAULT, fileName);
                                        log.info("service file ext => {}", strExt);
                                        String strFileSize = FileUtil.getFileSize(FileUtil.ALLOW_FILE_MAX_SIZE_DEFAULT, fileSize);
                                        log.info("service upload file size => {}", strFileSize);
                                        return fileService.upload(file_key, file_name, fileSize, awsProperties.getBucket(), buf)
                                                .flatMap(res -> {
                                                    log.info("service upload res   =>       {}", res);
                                                    log.info("service upload fileName   =>       {}", fileName.toString());
                                                    if (StringUtils.hasLength(organization))
                                                        historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "??????", organization, account);
                                                    if (StringUtils.hasLength(result))
                                                        historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "??????", result, account);
                                                    if (StringUtils.hasLength(reference))
                                                        historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "??????", reference, account);
                                                    historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "?????? ??????", Normalizer.normalize(file_name, Normalizer.Form.NFC), account);

                                                    return reviewEstimateDomainService.save(
                                                            ReviewEstimate.builder()
                                                                    .id(UUID.randomUUID().toString())
                                                                    .projectId(projectId)
                                                                    .organization(organization)
                                                                    .result(result)
                                                                    .reference(reference)
                                                                    .fileStatus(FileStatus.ING)
                                                                    .fileKey(file_key)
                                                                    .fileName(Normalizer.normalize(file_name, Normalizer.Form.NFC))
                                                                    .createDate(LocalDateTime.now())
                                                                    .createAdminAccountId(account.getAccountId())
                                                                    .build()
                                                    ).flatMap(r1 -> {
                                                        // ?????? ????????? ???????????? s3 ????????? ????????? ??? ????????? SQS??? ?????? ????????? ????????????.
                                                        awsSQSSender.sendMessage(makeReviewSqsData(r1), UUID.randomUUID().toString());
                                                        return Mono.just(r1);
                                                    });
                                                });
                                    });
                        } else { // ?????? ??????????????? ?????? ???????????? ??????.
                            if (StringUtils.hasLength(organization))
                                historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "?????? ??????", organization, account);
                            if (StringUtils.hasLength(result))
                                historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "??????", result, account);
                            if (StringUtils.hasLength(reference))
                                historyLog.send(projectId, "???????????? ??????>?????? ??????", "?????? ??????", "??????", reference, account);

                            // ?????? ???????????? ??????.
                            return reviewEstimateDomainService.save(
                                    ReviewEstimate.builder()
                                            .id(UUID.randomUUID().toString())
                                            .projectId(projectId)
                                            .organization(organization)
                                            .result(result)
                                            .reference(reference)
                                            .fileStatus(FileStatus.NO)
                                            .fileKey(fileKey)
                                            .fileName(fileName)
                                            .createDate(LocalDateTime.now())
                                            .createAdminAccountId(account.getAccountId())
                                            .build()
                            );
                        }
                    }


                }).collectList()
                .then(this.findByProjectId(reviewEstimateRequest.getProjectId().get(0)));  //???????????? ?????? ???????????? ???
    }

    /**
     * SQS ?????? ????????? ??????
     *
     * @param reviewEstimate
     * @return
     */
    private BucketUploadRequest makeReviewSqsData(ReviewEstimate reviewEstimate) {
        return BucketUploadRequest.builder()
                .bucketName(awsProperties.getBucket())
                .accountId(reviewEstimate.getCreateAdminAccountId())
                .roleType(RoleType.ADMIN)
                .sysType("LRC")
                .fileKey(reviewEstimate.getFileKey())
                .fileStatus(reviewEstimate.getFileStatus())
                .tableName("lrc_project_review_estimate")
                .build();
    }
}
