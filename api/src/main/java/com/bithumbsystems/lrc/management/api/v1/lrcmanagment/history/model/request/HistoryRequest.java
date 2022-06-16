package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryRequest {

    private String menu;            //메뉴
    private String subject;         //항목
    private String taskHistory;     //작업내역
    private String customer;        //수정자
}
