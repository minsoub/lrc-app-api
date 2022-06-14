package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserAccountRequest {

    List<User> userLists;

    @Data
    public static class User {
        private String id;
        private String projectId;               //프로젝트 코드
        private String userAccountId;           //회원 아이디
        private String userType;                //마스터/담당자구분
    }
}
