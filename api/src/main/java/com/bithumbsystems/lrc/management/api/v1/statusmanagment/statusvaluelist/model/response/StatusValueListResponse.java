package com.bithumbsystems.lrc.management.api.v1.statusmanagment.statusvaluelist.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StatusValueListResponse {

    private String id;
    private String name;        //상태명
    private String code;        //상태코드
    private String level;       //트리 레벨
    private Integer order;       //순서
    private String groupCode;   //분류코드
    private String parentCode;  //상위분류코드
    private Boolean useYn;      //사용 여부
    private List<StatusValueListResponse> children; //하위 레벨

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id
}
