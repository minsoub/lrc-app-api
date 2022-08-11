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
    @Schema(description = "Account User ID")
        private String id;

        @Schema(description = "Email")
        private String email;

}
