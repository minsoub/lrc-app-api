package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountResponse {

    private String id;
    private String projectId;               //프로젝트 코드
    private String userAccountId;           //회원 아이디
    private String userType;                //마스터/담당자구분
}
