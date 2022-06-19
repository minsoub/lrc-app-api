package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.foundationinfo.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoundationInfoRequest {

    private String id;
    private String projectId;       //프로젝트 id
    private String projectName;     //프로젝트 명
    private String symbol;          //심볼
    private String contrectCode;    //계약상태 code
    private String contrectName;    //계약상태
    private String progressCode;    //진행상태 code
    private String progressName;    //진행상태
    private String adminMemo;       //관리자 메모
}