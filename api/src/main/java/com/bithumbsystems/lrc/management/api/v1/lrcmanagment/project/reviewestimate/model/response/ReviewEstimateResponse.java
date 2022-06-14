package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEstimateResponse {

    private String id;
    private String projectId;       //프로젝트 코드
    private String organization;    //평가 기관
    private String result;          //평가 결과
    private String reference;       //평가 자료
}
