package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.icoinfo.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IcoInfoResponse {

    private String id;
    private String projectId;          //프로젝트 코드
    private String marketInfo;          //마켓 정보
    private Long price;                 //상장가(원)
    private LocalDateTime icoDate;      //상장일
}
