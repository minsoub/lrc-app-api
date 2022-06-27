package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectlink.model.entity;

import lombok.Data;

@Data
public class LinkProjectCode {

    private String id;
    private String projectId;       //프로젝트 id
    private String symbol;          //심볼
}
