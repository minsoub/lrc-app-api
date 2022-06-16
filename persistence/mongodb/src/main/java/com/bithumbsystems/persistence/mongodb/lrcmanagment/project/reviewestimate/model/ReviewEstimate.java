package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("lrc_project_review_estimate")
public class ReviewEstimate {

    @Id
    private String id;
    private String projectId;       //프로젝트 코드
    private String organization;    //평가 기관
    private String result;          //평가 결과
    private String reference;       //평가 자료
    private String referenceFile;   //평가 자료 파일
}
