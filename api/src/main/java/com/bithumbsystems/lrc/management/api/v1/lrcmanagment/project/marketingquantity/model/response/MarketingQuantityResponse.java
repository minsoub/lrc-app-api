package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingQuantityResponse {

    private String id;
    private String projectId;           //프로젝트 id
    private String symbol;              //심볼
    private Double minimumQuantity;     //최소 지원 수량
    private Double actualQuantity;      //실제 상장 지원 수량
}
