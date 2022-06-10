package com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetworkListResponse {

    private String id;
    private String name;    //네트워크 계열 명
    private String code;    //네트워크 계열 코열

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id
}
