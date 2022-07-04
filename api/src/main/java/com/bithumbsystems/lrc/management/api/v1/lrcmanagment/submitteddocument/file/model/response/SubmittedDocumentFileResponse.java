package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.response;


import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "제출 서류 관리 file")
public class SubmittedDocumentFileResponse {

    private String id;

    @Schema(description = "프로젝트 코드")
    private String projectId;       //프로젝트 코드

    @Schema(description = "구분")
    private SubmittedDocumentEnums type;            //구분

    @Schema(description = "파일 키")
    private String fileKey;         //파일코드

    @Schema(description = "파일 명")
    private String fileName;        //파일명

    @Schema(description = "작성자 메일 주소")
    private String email;

    @Schema(description = "생성 날짜")
    private LocalDateTime createDate; //생성날짜

    @Schema(description = "생성자 id")
    private String createAdminAccountId;  //생성자 id

    @Schema(description = "사용자 id")
    private String createAccountId;
}
