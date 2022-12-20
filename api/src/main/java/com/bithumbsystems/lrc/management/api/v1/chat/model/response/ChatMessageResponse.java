package com.bithumbsystems.lrc.management.api.v1.chat.model.response;

import com.bithumbsystems.persistence.mongodb.chat.model.enums.ChatRole;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChatMessageResponse {
    String id;
    String accountId;
    String email;
    String name;
    ChatRole role;
    String content;
    String chatRoom;
    LocalDateTime createDate;
}