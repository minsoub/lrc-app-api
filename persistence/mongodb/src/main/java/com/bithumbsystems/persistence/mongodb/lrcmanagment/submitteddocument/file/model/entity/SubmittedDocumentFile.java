package com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.model.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document("lrc_project_submitted_document_file")
public class SubmittedDocumentFile {

    @Id
    private String id;
    private String projectId;       //프로젝트 코드
    private String type;            //구분
    private String fileKey;         //파일코드
    private String fileName;        //파일명

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id
}
