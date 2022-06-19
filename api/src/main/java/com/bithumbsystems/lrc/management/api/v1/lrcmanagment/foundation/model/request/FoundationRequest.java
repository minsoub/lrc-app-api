package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.foundation.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoundationRequest {

    private String id;
    private String projectId;      //프로젝트 id
    private String ProjectName;     //프로젝트명
    private String symbol;          //심볼
    private String contrectCode;    //계약상태 code
    private String contrectName;    //계약상태
    private String progressCode;    //진행상태 code
    private String progressName;    //진행상태
    private String businessList;    //사업계열
    private String networkList;     //네트워크계열
    private String marketingMin;    //마케팅 수량 최소
    private String marketingCurrent;   //마케팅 수량 실제
    private String projectLink;     //연결프로젝트
    private LocalDateTime ipoDate;         //상장일

    private LocalDateTime createDate; //생성날짜
}
