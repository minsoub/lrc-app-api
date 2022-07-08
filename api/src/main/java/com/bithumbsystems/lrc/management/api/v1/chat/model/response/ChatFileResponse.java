package com.bithumbsystems.lrc.management.api.v1.chat.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
@Builder
public class ChatFileResponse {
    private String id; // file key
    private String projectId;
    private String fileName;
    private String fileSize;
    private String fileType;
    private LocalDateTime createDate;
    private String createAccountId;
}
