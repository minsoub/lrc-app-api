package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.marketingquantity.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "마케팅 수 정보")
public class MarketingQuantityRequest {

    @Schema(description = "마케팅 수량 정보 리스트")
    List<Marketing> marketingList;

    @Data
    public static class Marketing {
        private String id;

        @Schema(description = "프로젝트 코드")
        private String projectId;           //프로젝트 id

        @Schema(description = "심볼")
        private String symbol;              //심볼

        @Schema(description = "최조 지원 수량")
        private Double minimumQuantity;     //최소 지원 수량

        @Schema(description = "실제 상장 지원 수량")
        private Double actualQuantity;      //실제 상장 지원 수량
    }
}
