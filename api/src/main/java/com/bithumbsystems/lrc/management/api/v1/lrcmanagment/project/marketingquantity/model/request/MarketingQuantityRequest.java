package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MarketingQuantityRequest {

    List<Marketing> marketingList;

    @Data
    public static class Marketing {
        private String id;
        private String projectId;           //프로젝트 id
        private String symbol;              //심볼
        private Long minimumQuantity;     //최소 지원 수량
        private Long actualQuantity;      //실제 상장 지원 수량
    }
}
