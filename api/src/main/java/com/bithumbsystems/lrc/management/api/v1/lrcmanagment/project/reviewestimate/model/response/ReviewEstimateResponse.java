package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.response;

import com.bithumbsystems.persistence.mongodb.chat.model.enums.FileStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEstimateResponse {

    private String id;

    @Schema(description = "프로젝트 코드")
    private String projectId;       //프로젝트 코드

    @Schema(description = "평가 기관")
    private String organization;    //평가 기관

    @Schema(description = "평가 결과")
    private String result;          //평가 결과

    @Schema(description = "평가 자료")
    private String reference;       //평가 자료

    @Schema(description = "파일 상태")
    private FileStatus fileStatus;

    @Schema(description = "평가 자료 파일")
    private String fileKey;   //평가 자료 파일

    @Schema(description = "파일명")
    private String fileName;

    @Schema(description = "생성날짜")
    private LocalDateTime createDate; //생성날짜

    @Schema(description = "생성자 id")
    private String createAdminAccountId;  //생성자 id

    @Schema(description = "수정날짜")
    private LocalDateTime updateDate; //수정날짜

    @Schema(description = "수정자 id")
    private String updateAdminAccountId; //수정자 id
}
