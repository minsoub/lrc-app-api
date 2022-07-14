package com.bithumbsystems.lrc.management.api.v1.chat.controller;

import com.bithumbsystems.lrc.management.api.core.config.resolver.Account;
import com.bithumbsystems.lrc.management.api.core.config.resolver.CurrentUser;
import com.bithumbsystems.lrc.management.api.core.model.response.MultiResponse;
import com.bithumbsystems.lrc.management.api.core.model.response.SingleResponse;
import com.bithumbsystems.lrc.management.api.v1.chat.model.request.ChatFileRequest;
import com.bithumbsystems.lrc.management.api.v1.chat.model.request.ChatRequest;
import com.bithumbsystems.lrc.management.api.v1.chat.model.response.ChatFileResponse;
import com.bithumbsystems.lrc.management.api.v1.chat.service.ChatService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Comparator;

import static org.springframework.http.MediaType.MULTIPART_FORM_DATA_VALUE;

@RequiredArgsConstructor
@RestController
@RequestMapping("lrc/service")
@Tag(name = "Chat 서비스 관리", description = "Chat 서비스 관리 API")
public class ChatController {

    private final ChatService chatService;

    @PutMapping("/chat")
    @Operation(summary = "chat 참여 여부 조회 및 생성" , description = "chat 참여 여부 조회 및 생성", tags = "chat > chat 참여 여부 조회 및 생성")
    public ResponseEntity<Mono<?>> getChatInfoAndSave(@RequestBody ChatRequest chatRequest,
                                                     @Parameter(hidden = true) @CurrentUser Account account) {

        return ResponseEntity.ok().body(chatService.chatChannelCheckAndAdd(chatRequest, account)
                .map(SingleResponse::new));
    }

    @GetMapping("/chat/files/{id}")
    @Operation(summary = "파일 리스트 정보 조회", description = "projectId를 이용하여 파일리스트를 조회합니다.", tags = "chat > chat 파일 리스트 정보 보회")
    public ResponseEntity<Mono<?>> getFileList(@Parameter(name = "id", description = "project 의 id", in = ParameterIn.PATH)
                                                     @PathVariable("id") String id) {
        return ResponseEntity.ok().body(chatService.findByFileList(id)
                        .collectSortedList(Comparator.comparing(ChatFileResponse::getCreateDate))
                .map(c -> new MultiResponse(c))
        );
    }

    /**
     * 제출 서류 관리 file 저장
     * @param chattFileRequest
     * @return ChatFileResponse Object
     */
    @PostMapping(value = "/chat/file", consumes = MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Chat file 저장", description = "Chat file 정보를 저장 합니다.", tags = "chat / 파일저장")
    public ResponseEntity<Mono<?>> chatFileUpload(@ModelAttribute(value = "chattFileRequest") ChatFileRequest chattFileRequest,
                                                               @Parameter(hidden = true) @CurrentUser Account account) {
        return ResponseEntity.ok().body(chatService.fileSave(Mono.just(chattFileRequest), account)
                .map(c -> new SingleResponse(c)));
    }
}
