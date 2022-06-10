package com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessListRequest {

    private String name;    //사업 계열 명
    private String code;    //사업 계열 코드
}
