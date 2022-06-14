package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.projectinfo.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfoRequest {

    private String projectId;               //프로젝트 코드
    private String businessList;            //사업계열
    private String networkList;             //네트워크계열
    private String whitepaperLink;          //백서링크
    private String contractAddress;         //컨트렉트 주소
    private LocalDateTime createDate;       //최초 발행일
}
