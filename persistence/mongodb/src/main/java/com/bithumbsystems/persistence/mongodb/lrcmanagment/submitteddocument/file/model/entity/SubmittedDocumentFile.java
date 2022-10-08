package com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.model.entity;


import com.bithumbsystems.persistence.mongodb.chat.model.enums.FileStatus;
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
@Document("lrc_project_submitted_document_file")
public class SubmittedDocumentFile {

    @MongoId(value = FieldType.STRING, targetType = FieldType.STRING)
    private String id;
    private String projectId;       //프로젝트 코드
    private SubmittedDocumentEnums type;            //구분
    private String fileKey;         //파일코드
    private String fileName;        //파일명
    private FileStatus fileStatus;  // 파일 상태
    private String email;           // 작성자 메일 주소

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id
    private String createAccountId; // 사용자 id
}
