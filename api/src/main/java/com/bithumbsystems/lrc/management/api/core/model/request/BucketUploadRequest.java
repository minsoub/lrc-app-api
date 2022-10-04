package com.bithumbsystems.lrc.management.api.core.model.request;

import com.bithumbsystems.persistence.mongodb.audit.model.enums.RoleType;
import com.bithumbsystems.persistence.mongodb.chat.model.enums.FileStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class BucketUploadRequest {
    private String bucketName;
    private String fileKey;
    private RoleType roleType;  // ADMIN, USER
    private FileStatus fileStatus;  // File Status
    private String sysType;     // 시스템 구분 (LRC, CPC 등)
    private String tableName;   // Table 구분 (lrc_project_review_estimate(검토평가), lrc_chat_file (Chat file))
    private String accountId;   // 계정 ID
}
