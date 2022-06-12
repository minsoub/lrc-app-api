package com.bithumbsystems.lrc.management.api.v1.servicelog.model.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceLogResponse {

    private String id;          //uuid;
    private String seq;         //SN
    private String account;     //user id;
    private String ip;          //사용자 ip
    private String menuId;      //메뉴 id
    private String menuName;    //메뉴명
    private String crud;        //crud
    private String uri;         //url
    private String parameter;   //파라미터
    private String referer;
    private String device;      //장비
    private String result;      //성공/실패 여부
    private String message;     //메시지 정보
    private LocalDateTime createDdate;   //발행일시
}
