package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("lrc_project_marketing_quantity")
public class MarketingQuantity {

    @Id
    private String id;
    private String projectId;           //프로젝트 id
    private String symbol;              //심볼
    private Double minimumQuantity;     //최소 지원 수량
    private Double actualQuantity;      //실제 상장 지원 수량
    private Boolean delYn;              // 삭제 여부
}
