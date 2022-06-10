package com.bithumbsystems.lrc.management.api.v1.statusmanagment.businesslist.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BusinessListResponse {

    private String id;
    private String name;    //사업 계열 명
    private String code;    //사업 계열 코드

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id
}
