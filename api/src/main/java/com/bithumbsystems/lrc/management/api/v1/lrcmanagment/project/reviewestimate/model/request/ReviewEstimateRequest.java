package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReviewEstimateRequest {

    private String id;
    private String projectId;       //프로젝트 코드
    private String organization;    //평가 기관
    private String result;          //평가 결과
    private String reference;       //평가 자료
    private String fileKey;         //평가 자료 파일
    private FilePart filePart;
//    List<ReviewEstimate> reviewEstimateList;
}
