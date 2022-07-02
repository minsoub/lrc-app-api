package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "상장 정보")
public class IcoInfoRequest {

    @Schema(description = "상장 정보 리스트")
    private List<Ico> icoInfoList;

    @Data
    public static class Ico {
        private String id;

        @Schema(description = "프로젝트 코드")
        private String projectId;          //프로젝트 코드

        @Schema(description = "마켓 정보")
        private String marketInfo;          //마켓 정보

        @Schema(description = "상장가(원)")
        private Long price;                 //상장가(원)

        @Schema(description = "상장일")
        private LocalDate icoDate;      //상장일
    }
}


