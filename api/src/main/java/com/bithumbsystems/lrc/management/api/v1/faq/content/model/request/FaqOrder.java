package com.bithumbsystems.lrc.management.api.v1.faq.content.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "FAQ 노출 순서")
public class FaqOrder {
    @Schema(description = "FAQ id")
    private String id;      //사용자 id

    @Schema(description = "노출순서")
    private Integer order;       //노출순서
}
