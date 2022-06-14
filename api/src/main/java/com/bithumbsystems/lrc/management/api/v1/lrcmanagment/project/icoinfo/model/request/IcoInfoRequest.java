package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IcoInfoRequest {

    private List<Ico> icoInfoList;

    @Data
    public static class Ico {
        private String id;
        private String projectId;          //프로젝트 코드
        private String marketInfo;          //마켓 정보
        private Long price;                 //상장가(원)
        private LocalDateTime icoDate;      //상장일
    }
}


