package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.model.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SubmittedDocumentRequest {

    private String id;
    private String projectId;       //프로젝트 코드
    private String type;            //구분
    private String url;             //url
    private String fileKey;         //파일코드
    private String fileName;        //파일명
    private FilePart filePart;      //첨부파일
}
