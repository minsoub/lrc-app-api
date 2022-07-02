package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.reviewestimate.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.codec.multipart.FilePart;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "검토 평가")
public class ReviewEstimateRequest {

    private List<Integer> no;
    private List<String> id;

    @Schema(description = "프로젝트 코드")
    private List<String> projectId;       //프로젝트 코드

    @Schema(description = "평가 기관")
    private List<String> organization;    //평가 기관

    @Schema(description = "평가 결과")
    private List<String> result;          //평가 결과

    @Schema(description = "평가 자료")
    private List<String> reference;       //평가 자료 (URL)

    @Schema(description = "평가 자료 파일")
    private List<String> fileKey;         //평가 자료 파일

    @Schema(description = "파일 전송여부")
    private List<Boolean> isFile;

    @Schema(description = "평가 자료 파일 오브젝트")
    private List<FilePart> file;

    @Schema(description = "파일명")
    private List<String> fileName;


    //private List<ReviewEstimateRequest> reviewEstimateList = new ArrayList<>();
}
