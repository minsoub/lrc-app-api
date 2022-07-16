package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document("lrc_project_ico_info")
public class IcoInfo {

    @Id
    private String id;
    private String projectId;          //프로젝트 코드
    private String marketInfo;          //마켓 정보
    private Double price;                 //상장가(원)
    private LocalDate icoDate;      //상장일
}
