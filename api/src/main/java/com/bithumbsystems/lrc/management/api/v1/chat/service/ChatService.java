package com.bithumbsystems.lrc.management.api.v1.chat.service;

import com.bithumbsystems.lrc.management.api.core.config.property.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.v1.chat.model.request.ChatFileRequest;
import com.bithumbsystems.lrc.management.api.v1.chat.model.request.ChatRequest;
import com.bithumbsystems.lrc.management.api.v1.chat.model.response.ChatFileResponse;
import com.bithumbsystems.lrc.management.api.v1.chat.model.response.ChatResponse;
import com.bithumbsystems.lrc.management.api.v1.file.service.FileService;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.mapper.SubmittedDocumentFileMapper;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.request.SubmittedDocumentFileRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.response.SubmittedDocumentFileResponse;
import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatFile;
import com.bithumbsystems.persistence.mongodb.chat.model.enums.ChatRole;
import com.bithumbsystems.persistence.mongodb.chat.service.ChatDomainService;
import com.bithumbsystems.persistence.mongodb.file.model.entity.File;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.model.entity.SubmittedDocumentFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.nio.ByteBuffer;
import java.text.Normalizer;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatDomainService chatDomainService;

    private final AwsProperties awsProperties;
    private final FileService fileService;

    /**
     * 사용자 존재하는 체크해서 없으면 채팅방에 신규 등록
     * 있으면 해당 방에 프로젝트아이디가 존재하는지 체크하고 없으면 추가해서 저장한다.
     *
     * @param chatRequest
     * @param account
     * @return
     */
    public Mono<ChatResponse> chatChannelCheckAndAdd(ChatRequest chatRequest, Account account) {
        return chatDomainService.findByAccountId(account.getAccountId())
                .flatMap(result -> {
                    // 방이 여러개인데 해당 방이 존재하는지 체크한다.
                    // 방이 존재하면 ok 없으면 추가해야 한다.
                    if (result.getChatRooms().contains(chatRequest.getProjectId())) {
                        return Mono.just(ChatResponse.builder().isUse(true).build());
                    }else {
                        Set<String> roomList = result.getChatRooms();
                        roomList.add(chatRequest.getProjectId());
                        result.setChatRooms(roomList);
                        return chatDomainService.update(result)
                                .flatMap(r -> {
                                    return Mono.just(ChatResponse.builder().isUse(true).build());
                                });
                    }
                })
                .switchIfEmpty(
                        chatDomainService.insert(ChatChannel.builder()
                                .id(UUID.randomUUID().toString())
                                .accountId(account.getAccountId())
                                .siteId(chatRequest.getSiteId())
                                .role(ChatRole.ADMIN)
                                .chatRooms(Set.of(chatRequest.getProjectId()))
                                .build())
                                .flatMap(result -> {
                                    return Mono.just(ChatResponse.builder().isUse(true).build());
                                })
                                .switchIfEmpty(Mono.just(ChatResponse.builder().isUse(false).build()))
                );
    }

    /**
     * 채팅 파일 리스트 정보를 리턴한다.
     *
     * @param projectId
     * @return
     */
    public Flux<ChatFileResponse> findByFileList(String projectId) {
        return chatDomainService.findByList(projectId)
                .flatMap(result -> {
                   return Mono.just(ChatFileResponse.builder()
                           .id(result.getId())
                           .projectId(result.getProjectId())
                           .fileName(result.getFileName())
                           .fileSize(result.getFileSize())
                           .fileType(result.getFileType())
                           .createDate(result.getCreateDate())
                           .createAccountId(result.getCreateAccountId())
                           .build());
                });

    }
    /**
     * 제출 서류 관리 file 저장
     * @param fileRequest
     * @return SubmittedDocumentResponse Object
     */
    public Mono<ChatFileResponse> fileSave(Mono<ChatFileRequest> fileRequest, Account account) {
        return fileRequest
                .flatMap(request -> {
                            return DataBufferUtils.join(request.getFile().content())
                                    .flatMap(dataBuffer -> {
                                        ByteBuffer buf = dataBuffer.asByteBuffer();
                                        String fileKey = UUID.randomUUID().toString();
                                        String fileName = request.getFile().filename();
                                        Long fileSize = (long) buf.array().length;
                                        log.info("byte size ===>  {}   :   {}   :   {} : ", fileKey, fileName, fileSize);

                                        return fileService.upload(fileKey, fileName, fileSize, awsProperties.getBucket(), buf)
                                                .publishOn(Schedulers.boundedElastic())
                                                .flatMap(res -> {
                                                    log.info("service upload res   =>       {}", res);
                                                    log.info("service upload fileName   =>       {}", fileName.toString());
                                                    return chatDomainService.save(
                                                            ChatFile.builder()
                                                                    .id(fileKey)
                                                                    .fileName(request.getFileName())
                                                                    .fileType(request.getFileType())
                                                                    .fileSize(request.getFileSize())
                                                                    .projectId(request.getProjectId())
                                                                    .createAccountId(account.getAccountId())
                                                                    .createDate(LocalDateTime.now())
                                                                    .build()
                                                    );
                                                });

                                    });
                        }
                )
                .map(res -> ChatFileResponse.builder()
                        .id(res.getId())
                        .projectId(res.getProjectId())
                        .fileType(res.getFileType())
                        .fileSize(res.getFileSize())
                        .fileName(res.getFileName())
                        .createAccountId(res.getCreateAccountId())
                        .createDate(res.getCreateDate())
                        .build());
    }

    /**
     * 채팅 메시지를 삭제한다.
     *
     * @param id
     * @param account
     * @return
     */
    public Mono<ChatResponse> deleteMessage(String id, Account account) {
        return chatDomainService.findById(id)
                .flatMap(result -> {
                    return chatDomainService.delete(result)
                            .flatMap(s -> {
                                return Mono.just(ChatResponse.builder().isUse(true).build());
                            });
                });
    }
}
