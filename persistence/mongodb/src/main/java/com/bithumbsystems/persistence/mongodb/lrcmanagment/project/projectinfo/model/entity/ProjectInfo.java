package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document("lrc_project_info")
public class ProjectInfo {

    @Id
    private String id;
    private String projectId;               //프로젝트 코드
    private String businessLine;            //사업계열
    private String networkLine;             //네트워크계열
    private String whitepaperLink;          //백서링크
    private String contractAddress;         //컨트렉트 주소
    private LocalDateTime createDate;       //최초 발행일
}
