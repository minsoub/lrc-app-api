package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.model.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Document("lrc_project_foundation_info")
public class FoundationInfo {

    @Id
    private String id;
    private String name;     //프로젝트 명
    private String symbol;          //심볼
    private String contractCode;    //계약상태 code
    private String processCode;    //진행상태 code
    private String memo;       //관리자 메모

    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id
}