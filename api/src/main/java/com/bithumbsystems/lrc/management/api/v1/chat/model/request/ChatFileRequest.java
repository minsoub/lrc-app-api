package com.bithumbsystems.lrc.management.api.v1.chat.model.request;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Chat file")
public class ChatFileRequest {
    @Schema(description = "프로젝트 코드")
    private String projectId;       //프로젝트 코드

    @Schema(description = "파일타입")
    private String fileType;            //파일타입

    @Schema(description = "파일 키")
    private String fileSize;         //파일사이즈

    @Schema(description = "파일 명")
    private String fileName;        //파일명

    @Schema(description = "파일 Object")
    private FilePart file;      //첨부파일
}
