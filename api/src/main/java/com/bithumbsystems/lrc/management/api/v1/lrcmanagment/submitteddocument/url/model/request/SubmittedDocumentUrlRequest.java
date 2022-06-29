package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.model.request;


import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "제출 서류 관리 url")
public class SubmittedDocumentUrlRequest {

    @Schema(description = "프로젝트 코드")
    private String projectId;       //프로젝트 코드

    @Schema(description = "구분")
    private SubmittedDocumentEnums type;            //구분

    @Schema(description = "url")
    private String url;             //url

//    @Schema(description = "파일 Object")
//    private FilePart filePart;      //첨부파일
}
