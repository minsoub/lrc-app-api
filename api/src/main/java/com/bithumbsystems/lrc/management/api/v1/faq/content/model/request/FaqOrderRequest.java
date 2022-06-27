package com.bithumbsystems.lrc.management.api.v1.faq.content.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "FAQ 노출 순서")
public class FaqOrderRequest {

    @Schema(description = "FAQ Order List")
    private List<FaqOrder> orderList;
}

