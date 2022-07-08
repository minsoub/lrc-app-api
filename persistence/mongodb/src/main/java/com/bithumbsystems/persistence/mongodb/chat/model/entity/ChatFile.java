package com.bithumbsystems.persistence.mongodb.chat.model.entity;

import com.bithumbsystems.persistence.mongodb.chat.model.enums.ChatRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "lrc_chat_file")
@AllArgsConstructor
@Data
@Builder
public class ChatFile {
    @MongoId
    private String id;
    private String projectId;
    private String fileName;
    private String fileSize;
    private String fileType;
    private LocalDateTime createDate; //생성날짜
    private String createAccountId;  //생성자 id
}
