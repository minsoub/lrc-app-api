package com.bithumbsystems.lrc.management.api.v1.chat.service;

import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode;
import com.bithumbsystems.lrc.management.api.core.model.request.BucketUploadRequest;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.lrc.management.api.core.util.FileUtil;
import com.bithumbsystems.lrc.management.api.core.util.sender.AwsSQSSender;
import com.bithumbsystems.lrc.management.api.v1.chat.model.request.ChatFileRequest;
import com.bithumbsystems.lrc.management.api.v1.chat.model.request.ChatRequest;
import com.bithumbsystems.lrc.management.api.v1.chat.model.response.ChatFileResponse;
import com.bithumbsystems.lrc.management.api.v1.chat.model.response.ChatResponse;
import com.bithumbsystems.lrc.management.api.v1.file.exception.FileException;
import com.bithumbsystems.lrc.management.api.v1.file.service.FileService;
import com.bithumbsystems.persistence.mongodb.account.service.AccountDomainService;
import com.bithumbsystems.persistence.mongodb.audit.model.enums.RoleType;
import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatChannel;
import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatFile;
import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatMessage;
import com.bithumbsystems.persistence.mongodb.chat.model.enums.ChatRole;
import com.bithumbsystems.persistence.mongodb.chat.model.enums.FileStatus;
import com.bithumbsystems.persistence.mongodb.chat.model.enums.UserType;
import com.bithumbsystems.persistence.mongodb.chat.service.ChatDomainService;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.model.entity.ReviewEstimate;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.service.UserInfoDomainService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import software.amazon.awssdk.core.async.AsyncResponseTransformer;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatDomainService chatDomainService;

    private final AccountDomainService accountDomainService;
    private final UserInfoDomainService userAccountDomainService;

    private final AwsProperties awsProperties;
    private final FileService fileService;

    private final S3AsyncClient s3AsyncClient;

    private final AwsSQSSender<BucketUploadRequest> awsSQSSender;
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
    public Mono<InputStream> download(String fileKey, String bucketName) {

        GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                .bucket(bucketName)
                .key(fileKey)
                .build();

        return Mono.fromFuture(
                s3AsyncClient.getObject(getObjectRequest, AsyncResponseTransformer.toBytes())
                        .thenApply(bytes -> {
                            return bytes.asInputStream();  // .asByteArray(); // ResponseBytes::asByteArray
                        })
                        .whenComplete((res, error) -> {
                                    try {
                                        if (res != null) {
                                            log.debug("whenComplete -> {}", res);
                                        }else {
                                            error.printStackTrace();
                                        }
                                    }finally {
                                        //s3AsyncClient.close();
                                    }
                                }
                        )
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
                    if (result.getUserType() == null) {
                        return Mono.just(ChatFileResponse.builder()
                                .id(result.getId())
                                .projectId(result.getProjectId())
                                .fileName(result.getFileName())
                                .fileSize(result.getFileSize())
                                .fileType(result.getFileType())
                                .fileStatus(result.getFileStatus())
                                .userType(result.getUserType())
                                .userTypeName(null)
                                .createDate(result.getCreateDate())
                                .createAccountId(result.getCreateAccountId())
                                .build());
                    } else if (result.getUserType().equals(UserType.USER)) {
                        return userAccountDomainService.findById(result.getCreateAccountId())
                                .flatMap(r1 -> {
                                   return  Mono.just(ChatFileResponse.builder()
                                           .id(result.getId())
                                           .projectId(result.getProjectId())
                                           .fileName(result.getFileName())
                                           .fileSize(result.getFileSize())
                                           .fileType(result.getFileType())
                                           .fileStatus(result.getFileStatus())
                                           .userType(result.getUserType())
                                           .userTypeName(AES256Util.decryptAES(awsProperties.getKmsKey(), r1.getEmail()))
                                           .createDate(result.getCreateDate())
                                           .createAccountId(result.getCreateAccountId())
                                           .build());
                                });
                    }else if (result.getUserType().equals(UserType.ADMIN)) {
                        return accountDomainService.findByAdminId(result.getCreateAccountId())
                                .flatMap(r2 -> {
                                    return  Mono.just(ChatFileResponse.builder()
                                            .id(result.getId())
                                            .projectId(result.getProjectId())
                                            .fileName(result.getFileName())
                                            .fileSize(result.getFileSize())
                                            .fileType(result.getFileType())
                                            .fileStatus(result.getFileStatus())
                                            .userType(result.getUserType())
                                            .userTypeName(r2.getEmail())
                                            .createDate(result.getCreateDate())
                                            .createAccountId(result.getCreateAccountId())
                                            .build());
                                });
                    }
                    return null;
                });
    }

    public Mono<ChatFileResponse> findByFileInfo(String projectId, String fileKey) {
        return chatDomainService.findByFileId(fileKey)
                .flatMap(result -> {
                    if (result.getUserType() == null) {
                        return Mono.just(ChatFileResponse.builder()
                                .id(result.getId())
                                .projectId(result.getProjectId())
                                .fileName(result.getFileName())
                                .fileSize(result.getFileSize())
                                .fileType(result.getFileType())
                                 .fileStatus(result.getFileStatus())
                                .userType(result.getUserType())
                                .userTypeName(null)
                                .createDate(result.getCreateDate())
                                .createAccountId(result.getCreateAccountId())
                                .build());
                    } else if (result.getUserType().equals(UserType.USER)) {
                        return userAccountDomainService.findById(result.getCreateAccountId())
                                .flatMap(r1 -> {
                                    return  Mono.just(ChatFileResponse.builder()
                                            .id(result.getId())
                                            .projectId(result.getProjectId())
                                            .fileName(result.getFileName())
                                            .fileSize(result.getFileSize())
                                            .fileType(result.getFileType())
                                            .fileStatus(result.getFileStatus())
                                            .userType(result.getUserType())
                                            .userTypeName(AES256Util.decryptAES(awsProperties.getKmsKey(), r1.getEmail()))
                                            .createDate(result.getCreateDate())
                                            .createAccountId(result.getCreateAccountId())
                                            .build());
                                });
                    }else if (result.getUserType().equals(UserType.ADMIN)) {
                        return accountDomainService.findByAdminId(result.getCreateAccountId())
                                .flatMap(r2 -> {
                                    return  Mono.just(ChatFileResponse.builder()
                                            .id(result.getId())
                                            .projectId(result.getProjectId())
                                            .fileName(result.getFileName())
                                            .fileSize(result.getFileSize())
                                            .fileType(result.getFileType())
                                            .fileStatus(result.getFileStatus())
                                            .userType(result.getUserType())
                                            .userTypeName(r2.getEmail())
                                            .createDate(result.getCreateDate())
                                            .createAccountId(result.getCreateAccountId())
                                            .build());
                                });
                    }
                    return null;
                });
    }
    /**
     * 파일 상세 정보 조회
     * @param fileKey
     * @return
     */
    public Mono<ChatFile> findById(String fileKey) {
        return chatDomainService.findByChatFileId(fileKey);
    }
    /**
     * 제출 서류 관리 file 저장
     * @param fileRequest
     * @return SubmittedDocumentResponse Object
     */
    public Mono<ChatFileResponse> fileSave(Mono<ChatFileRequest> fileRequest, Account account) {
        return fileRequest
                .flatMap(request -> {
                    String mimeType = request.getFileType();
                    log.debug("application upload mimeType check => {}", mimeType);
                    // mimeType check
                    if(Arrays.asList(FileUtil.ALLOW_MIME_TYPE_DEFAULT).contains(mimeType.toUpperCase()) == false){
                        return Mono.error(new FileException(ErrorCode.INVALID_FILE_EXT));
                    }


                            return DataBufferUtils.join(request.getFile().content())
                                    .flatMap(dataBuffer -> {
                                        ByteBuffer buf = dataBuffer.asByteBuffer();
                                        String fileKey = UUID.randomUUID().toString();
                                        String fileName = request.getFile().filename();
                                        Long fileSize = (long) buf.array().length;
                                        log.info("byte size ===>  {}   :   {}   :   {} : ", fileKey, fileName, fileSize);

                                        String strExt = FileUtil.getFileExt(FileUtil.ALLOW_FILE_EXT_DEFAULT, fileName);
                                        log.info("service file ext => {}", strExt);
                                        String strFileSize = FileUtil.getFileSize(FileUtil.ALLOW_FILE_MAX_SIZE_DEFAULT, fileSize);
                                        log.info("service upload file size => {}", strFileSize);

                                        // Mime-type과 확장자 비교.
                                        if (FileUtil.allowContentType.containsKey(mimeType)) {
                                            List<String> extList = FileUtil.allowContentType.get(mimeType);
                                            if (!extList.contains(strExt)) {
                                                return Mono.error(new FileException(ErrorCode.INVALID_FILE_EXT));
                                            }
                                        } else {
                                            return Mono.error(new FileException(ErrorCode.INVALID_FILE_EXT));
                                        }

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
                                                                    .fileStatus(FileStatus.ING)
                                                                    .projectId(request.getProjectId())
                                                                    .userType(UserType.ADMIN)
                                                                    .createAccountId(account.getAccountId())
                                                                    .createDate(LocalDateTime.now())
                                                                    .build()
                                                    ).flatMap(r1 -> {
                                                        // 파일 업로드 했으므로 s3 상태를 조회할 수 있도록 SQS에 파일 정보를 전송한다.
                                                        awsSQSSender.sendMessage(makeReviewSqsData(r1), UUID.randomUUID().toString());
                                                        return Mono.just(r1);
                                                    });
                                                });

                                    });
                })
                .map(res -> ChatFileResponse.builder()
                        .id(res.getId())
                        .projectId(res.getProjectId())
                        .fileType(res.getFileType())
                        .fileSize(res.getFileSize())
                        .fileName(res.getFileName())
                        .fileStatus(res.getFileStatus())
                        .userType(UserType.ADMIN)
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


    /**
     * 엑셀 파일을 만들어서 리턴한다.
     *
     * @param roomId
     * @param siteId
     * @return
     */
    public Mono<ByteArrayInputStream> downloadExcel(String roomId, String siteId) {
        return chatDomainService.findChatMessage(roomId, siteId)
                //.map(AuditLogMapper.INSTANCE::auditLogResponse)
                .collectSortedList(Comparator.comparing(ChatMessage::getCreateDate))
                .flatMap(list -> this.createExcelFile(list));
    }

    /**
     * 엑셀 파일 생성.
     *
     * @param chatList
     * @return
     */
    private Mono<ByteArrayInputStream> createExcelFile(List<ChatMessage> chatList) {
        return Mono.fromCallable(() -> {
                    log.debug("엑셀 파일 생성 시작");

                    SXSSFWorkbook workbook = new SXSSFWorkbook(SXSSFWorkbook.DEFAULT_WINDOW_SIZE);  // keep 100 rows in memory, exceeding rows will be flushed to disk
                    ByteArrayOutputStream out = new ByteArrayOutputStream();

                    CreationHelper creationHelper = workbook.getCreationHelper();

                    Sheet sheet = workbook.createSheet("Chat Message");

                    Font headerFont = workbook.createFont();
                    headerFont.setFontName("맑은 고딕");
                    headerFont.setFontHeight((short) (10 * 20));
                    headerFont.setBold(true);
                    headerFont.setColor(IndexedColors.BLACK.index);

                    Font bodyFont = workbook.createFont();
                    bodyFont.setFontName("맑은 고딕");
                    bodyFont.setFontHeight((short) (10 * 20));

                    // Cell 스타일 생성
                    CellStyle headerStyle = workbook.createCellStyle();
                    headerStyle.setAlignment(HorizontalAlignment.CENTER);
                    headerStyle.setVerticalAlignment(VerticalAlignment.CENTER);
                    headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.index);
                    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    headerStyle.setFont(headerFont);

                    // Row for Header
                    Row headerRow = sheet.createRow(0);

                    // Header
                    String[] fields = {"구분", "이메일", "내용", "삭제여부", "작성일자"};
                    for (int col = 0; col < fields.length; col++) {
                        Cell cell = headerRow.createCell(col);
                        cell.setCellValue(fields[col]);
                        cell.setCellStyle(headerStyle);
                    }

                    // Body
                    int rowIdx = 1;
                    for (ChatMessage res : chatList) {
                        Row row = sheet.createRow(rowIdx++);

                        row.createCell(0).setCellValue(res.getRole().toString());
                        row.createCell(1).setCellValue(AES256Util.decryptAES(awsProperties.getKmsKey(), res.getEmail()));
                        row.createCell(2).setCellValue(AES256Util.decryptAES(awsProperties.getKmsKey(), res.getContent()));
                        row.createCell(3).setCellValue(res.getIsDelete());
                        row.createCell(4).setCellValue(res.getCreateDate().plusHours(9).format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                    }
                    workbook.write(out);

                    log.debug("엑셀 파일 생성 종료");
                    return new ByteArrayInputStream(out.toByteArray());
                })
                .log();
    }

    /**
     * SQS 전송 데이터 생성
     *
     * @param chatFile
     * @return
     */
    private BucketUploadRequest makeReviewSqsData(ChatFile chatFile) {
        return BucketUploadRequest.builder()
                .bucketName(awsProperties.getBucket())
                .accountId(chatFile.getCreateAccountId())
                .roleType(RoleType.ADMIN)
                .sysType("LRC")
                .fileKey(chatFile.getId())
                .fileStatus(chatFile.getFileStatus())
                .tableName("lrc_chat_file")
                .build();
    }
}
