package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.listener;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HistoryDto {
    private String id;
    private String projectId;
    private String menu;            //메뉴
    private String subject;         //항목
    private String item;            // 수정값
    private String taskHistory;     //작업내역
    private String type;            // 사용자 구분
    private String customer;        //수정자
    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id
    private String email;
    private String accountId;
}
