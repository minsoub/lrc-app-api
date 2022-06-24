package com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("lrc_project_history")
public class History {

    @Id
    private String id;
    private String menu;            //메뉴
    private String subject;         //항목
    private String taskHistory;     //작업내역
    private String customer;        //수정자

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id
}
