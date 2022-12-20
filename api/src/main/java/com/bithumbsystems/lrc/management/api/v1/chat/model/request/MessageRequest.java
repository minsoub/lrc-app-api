package com.bithumbsystems.lrc.management.api.v1.chat.model.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class MessageRequest {
    String content;
    String siteId;
    String name;
}