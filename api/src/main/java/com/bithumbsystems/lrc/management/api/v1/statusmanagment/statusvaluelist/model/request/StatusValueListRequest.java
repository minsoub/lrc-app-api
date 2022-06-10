package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatusValueListRequest {

    private String name;        //상태명
    private String code;        //상태코드
    private String level;      //트리 레벨
    private Integer order;       //순서
    private String groupCode;   //분류코드
    private String parentCode;  //상위분류코드
    private Boolean useYn;      //사용 여부
}
