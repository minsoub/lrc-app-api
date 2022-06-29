package com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.model.entity;


import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document("lrc_project_submitted_document_url")
public class SubmittedDocumentUrl {

    @MongoId(value = FieldType.STRING, targetType = FieldType.STRING)
    private String id;
    private String projectId;       //프로젝트 코드
    private SubmittedDocumentEnums type;            //구분
    private String url;             //url
    private String email;

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id
}
