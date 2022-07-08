package com.bithumbsystems.persistence.mongodb.chat.model.entity;

import com.bithumbsystems.persistence.mongodb.chat.model.enums.ChatRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.util.Set;

@Document(collection = "chat_channel")
@AllArgsConstructor
@Data
@Builder
public class ChatChannel {
    @MongoId
    private String id;
    private String accountId;
    private ChatRole role;
    private Set<String> chatRooms;
    private String siteId;
}
