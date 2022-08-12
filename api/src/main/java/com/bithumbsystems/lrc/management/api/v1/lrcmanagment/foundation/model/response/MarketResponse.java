package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MarketResponse {

    private Double minimumQuantity;
    private Double actualQuantity;
    private String symbol;
}
