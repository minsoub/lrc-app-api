package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "사용자 정보")
public class UserAccountRequest {

    @Schema(description = "사용자 정보 리스트")
    List<User> userLists;

    @Data
    public static class User {
        private String id;

        @Schema(description = "프로젝트 코드")
        private String projectId;               //프로젝트 코드

        @Schema(description = "회원 아이디")
        private String userAccountId;           //회원 아이디

        @Schema(description = "마스터/담당자구분")
        private String userType;                //마스터/담당자구분
    }
}
