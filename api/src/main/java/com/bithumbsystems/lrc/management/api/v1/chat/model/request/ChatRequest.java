package com.bithumbsystems.lrc.management.api.v1.chat.model.request;

import com.bithumbsystems.persistence.mongodb.chat.model.enums.ChatRole;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "채팅정보")
public class ChatRequest {
    private String accountId;
    private String siteId;
    private String projectId;
}
