package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmittedDocumentResponse {

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
