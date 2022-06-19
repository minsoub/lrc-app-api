package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Schema(description = "평가 자료 파일")
    private String fileKey;   //평가 자료 파일
}
