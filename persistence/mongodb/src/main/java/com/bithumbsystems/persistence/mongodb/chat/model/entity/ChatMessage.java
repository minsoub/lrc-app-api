package com.bithumbsystems.persistence.mongodb.chat.model.entity;

import com.bithumbsystems.persistence.mongodb.chat.model.enums.ChatRole;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document("chat_message")
@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatMessage {
    @Id
    private String id;
    private String accountId;
    private String email;
    private String name;
    private ChatRole role;
    private String content;
    private String chatRoom;
    private String siteId;
    private Boolean isDelete;
    private LocalDateTime deleteDate;
    private LocalDateTime createDate;
}
