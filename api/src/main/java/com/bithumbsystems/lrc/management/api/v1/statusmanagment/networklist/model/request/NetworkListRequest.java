package com.bithumbsystems.lrc.management.api.v1.statusmanagment.networklist.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NetworkListRequest {

    private String name;    //네트워크 계열 명
    private String code;    //네트워크 계열 코드
}
