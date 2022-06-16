package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryResponse {

    private String id;
    private String menu;            //메뉴
    private String subject;         //항목
    private String taskHistory;     //작업내역
    private String customer;        //수정자

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id
}
