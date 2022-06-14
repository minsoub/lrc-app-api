package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("lrc_project_ico_info")
public class IcoInfo {

    @Id
    private String id;
    private String projectId;          //프로젝트 코드
    private String marketInfo;          //마켓 정보
    private Long price;                 //상장가(원)
    private LocalDateTime icoDate;      //상장일
}
