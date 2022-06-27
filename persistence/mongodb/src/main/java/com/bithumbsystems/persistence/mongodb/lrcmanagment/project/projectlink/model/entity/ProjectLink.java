package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.model.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("lrc_project_link")
public class ProjectLink {

    @Id
    private String id;
    private String projectId;           //프로젝트 id
    private String symbol;              //심볼
    private String linkProjectId;       //링크 프로젝트 id
    private String linkProjectSymbol;   //링크 프로젝트 심볼
    private Boolean useYn;              //삭제
}
