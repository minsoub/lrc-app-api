package com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("lrc_project_submitted_document_status")
public class SubmittedDocument {

    @Id
    private String id;
    private String projectId;       //프로젝트 코드
    private String type;            //구분
    private String url;             //url
    private String fileKey;         //파일코드
    private String fileName;        //파일명

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id
}
