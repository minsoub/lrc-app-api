package com.bithumbsystems.lrc.management.api.v1.chat.controller;

import static com.bithumbsystems.lrc.management.api.core.model.enums.ErrorCode.INVALID_FILE;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE;
import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.chat.model.request.ChatFileRequest;
import com.bithumbsystems.lrc.management.api.v1.chat.model.request.ChatRequest;
import com.bithumbsystems.lrc.management.api.v1.chat.model.response.ChatFileResponse;
import com.bithumbsystems.lrc.management.api.v1.chat.service.ChatService;
import com.bithumbsystems.lrc.management.api.v1.exception.LrcException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/service")
@Tag(name = "Chat ????????? ??????", description = "Chat ????????? ?????? API")
public class ChatController {

    private final ChatService chatService;
    private final AwsProperties awsProperties;

    @PutMapping("/chat")
    @Operation(summary = "chat ?????? ?????? ?????? ??? ??????" , description = "chat ?????? ?????? ?????? ??? ??????", tags = "chat > chat ?????? ?????? ?????? ??? ??????")
    public ResponseEntity<Mono<?>> getChatInfoAndSave(@RequestBody ChatRequest chatRequest,
                                                     @Parameter(hidden = true) @CurrentUser Account account) {

        return ResponseEntity.ok().body(chatService.chatChannelCheckAndAdd(chatRequest, account)
                .map(SingleResponse::new));
    }

    /**
     * ?????? ?????? ??????
     * @param id
     * @return
     */
    @GetMapping("/chat/files/{id}")
    @Operation(summary = "?????? ????????? ?????? ??????", description = "projectId??? ???????????? ?????????????????? ???????????????.", tags = "chat > chat ?????? ????????? ?????? ??????")
    public ResponseEntity<Mono<?>> getFileList(@Parameter(name = "id", description = "project ??? id", in = ParameterIn.PATH)
                                                     @PathVariable("id") String id) {
        return ResponseEntity.ok().body(chatService.findByFileList(id)
                        .collectSortedList(Comparator.comparing(ChatFileResponse::getCreateDate).reversed())
                .map(c -> new MultiResponse(c))
        );
    }

    /**
     * ?????? ?????? ?????? ??????
     * @param id
     * @return
     */
    @GetMapping("/chat/files/{id}/{fileKey}")
    @Operation(summary = "?????? ?????? ?????? ??????", description = "projectId ??? ???????????? ???????????? ??????????????? ???????????????.", tags = "chat > chat ?????? ?????? ??????")
    public ResponseEntity<Mono<?>> getFileInfo(@Parameter(name = "id", description = "project ??? id", in = ParameterIn.PATH)
                                               @PathVariable("id") String id,
                                               @Parameter(name = "fileKey", description = "file ??? id", in = ParameterIn.PATH)
                                               @PathVariable("fileKey") String fileKey) {
        return ResponseEntity.ok().body(chatService.findByFileInfo(id, fileKey)
                .map(c -> new SingleResponse(c))
        );
    }

    /**
     * Chat file ??????
     * @param chattFileRequest
     * @return ChatFileResponse Object
     */
    @PostMapping(value = "/chat/file", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Chat file ??????", description = "Chat file ????????? ?????? ?????????.", tags = "chat / ????????????")
    public ResponseEntity<Mono<?>> chatFileUpload(@ModelAttribute(value = "chattFileRequest") ChatFileRequest chattFileRequest,
                                                               @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(chatService.fileSave(Mono.just(chattFileRequest), account)
                .map(c -> new SingleResponse(c)));
    }

    /**
     * ?????? ????????????
     * @param fileKey
     * @return
     */
    @GetMapping(value = "/chat/file/{fileKey}", produces = APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Chat file ????????????", description = "Chat file ????????? ???????????? ?????????.", tags = "chat / ??????????????????")
    public Mono<ResponseEntity<?>> s3download(@PathVariable String fileKey, @Parameter(hidden = true) @CurrentUser Account account) {

        AtomicReference<String> fileName = new AtomicReference<>();

        return chatService.findById(fileKey)
                .flatMap(res -> {
                    if (res.getCreateAccountId().equals(account.getAccountId())) {
                        return Mono.error(new LrcException(INVALID_FILE));
                    }
                    log.debug("find file => {}", res);
                    fileName.set(res.getFileName());
                    // s3?????? ????????? ???????????? ?????????.
                    return chatService.download(fileKey, awsProperties.getBucket());
                })
                .log()
                .map(inputStream -> {
                    log.debug("finaly result...here");
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentDispositionFormData(fileName.toString(), fileName.toString());
                    ResponseEntity<?> entity = ResponseEntity.ok().cacheControl(CacheControl.noCache())
                            .headers(headers)
                            .body(new InputStreamResource(inputStream));
                    return entity;
                });
    }

    /**
     * Chat Message ??????
     *
     * @param id
     * @param account
     * @return
     */
    @DeleteMapping("/chat/{id}")
    @Operation(summary = "Chat message ??????", description = "Chat Message ??????", tags = "chat/????????? ??????")
    public ResponseEntity<Mono<?>> deleteChatMessage(@Parameter(name="id", description = "Chat Message Id", in=ParameterIn.PATH)
                                                     @PathVariable("id") String id,
                                                     @Parameter(hidden = true) @CurrentUser Account account) {

        return ResponseEntity.ok().body(chatService.deleteMessage(id, account)
                .map(c -> new SingleResponse(c)));
    }


    @GetMapping(value = "/chat/excel/export", produces = APPLICATION_OCTET_STREAM_VALUE)
    @Operation(summary = "Chat Message - ?????? ????????????", description = "Chat Message: ?????? ????????????", tags = "chat/????????? ????????????")
    public Mono<ResponseEntity<?>> downloadExcel(@Parameter(name = "id", description = "chat room id", required = true) @RequestParam(required = false) String id,
                                                 @Parameter(hidden = true) @CurrentUser Account account) {

        return chatService.downloadExcel(id, account.getMySiteId())
                .flatMap(inputStream -> {
                    HttpHeaders headers = new HttpHeaders();
                    String fileName = "ChatMessage.xlsx";
                    headers.setContentDispositionFormData(fileName, fileName);
                    return Mono.just(ResponseEntity.ok().cacheControl(CacheControl.noCache())
                            .headers(headers)
                            .body(new InputStreamResource(inputStream)));
                });
    }
}
