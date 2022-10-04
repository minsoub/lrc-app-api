package com.bithumbsystems.lrc.management.api.v1.chat.model.response;

import com.bithumbsystems.persistence.mongodb.chat.model.enums.FileStatus;
import com.bithumbsystems.persistence.mongodb.chat.model.enums.UserType;
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
    private FileStatus fileStatus;
    private UserType userType;
    private String userTypeName;
    private LocalDateTime createDate;
    private String createAccountId;
}
