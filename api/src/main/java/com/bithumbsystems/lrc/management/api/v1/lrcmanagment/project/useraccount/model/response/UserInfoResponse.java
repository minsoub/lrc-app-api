package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.project.useraccount.model.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "거래지원 사용자 검색 정보")
public class UserInfoResponse {
    @Schema(description = "ID")
    private String userAccountId;
    @Schema(description = "Email")
    private String email;
}
