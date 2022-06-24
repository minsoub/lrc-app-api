package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Document("lrc_project_info")
public class ProjectInfo {

    @Id
    private String id;
    private String projectId;               //프로젝트 코드
    private String businessList;            //사업계열
    private String networkList;             //네트워크계열
    private String whitepaperLink;          //백서링크
    private String contractAddress;         //컨트렉트 주소
    private LocalDateTime createDate;       //최초 발행일

    public ProjectInfo(String projectId, String businessList, String networkList, String whitepaperLink, String contractAddress, LocalDateTime createDate) {
        this.id = UUID.randomUUID().toString();
        this.projectId = projectId;
        this.businessList = businessList;
        this.networkList = networkList;
        this.whitepaperLink = whitepaperLink;
        this.contractAddress = contractAddress;
        this.createDate = createDate;
    }
}
