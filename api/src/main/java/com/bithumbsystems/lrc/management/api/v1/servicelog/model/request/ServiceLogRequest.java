package com.bithumbsystems.lrc.management.api.v1.servicelog.model.request;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "서비스 로그 관리")
public class ServiceLogRequest {

    @Schema(description = "SN")
    private String seq;         //SN

    @Schema(description = "user id")
    private String account;     //user id;

    @Schema(description = "사용자 ip")
    private String ip;          //사용자 ip

    @Schema(description = "메뉴 id")
    private String menuId;      //메뉴 id

    @Schema(description = "메뉴명 id")
    private String menuName;    //메뉴명

    @Schema(description = "crud")
    private String crud;        //crud

    @Schema(description = "url")
    private String uri;         //url

    @Schema(description = "파라미터")
    private String parameter;   //파라미터

    @Schema(description = "referer")
    private String referer;

    @Schema(description = "장비")
    private String device;      //장비

    @Schema(description = "성공/실패 여부")
    private String result;      //성공/실패 여부

    @Schema(description = "메시지 정보")
    private String message;     //메시지 정보

    @Schema(description = "발행일시")
    private LocalDateTime createDdate;   //발행일시
}
