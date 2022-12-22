package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.response;

import com.bithumbsystems.persistence.mongodb.chat.model.enums.FileStatus;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.enums.SubmittedDocumentEnums;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Submitted document file response.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "제출 서류 관리 file")
public class SubmittedDocumentFileResponse {

  private String id;

  @Schema(description = "프로젝트 코드")
  private String projectId;

  @Schema(description = "구분")
  private SubmittedDocumentEnums type;

  @Schema(description = "파일 키")
  private String fileKey;

  @Schema(description = "파일 명")
  private String fileName;

  @Schema(description = "파일 상태")
  private FileStatus fileStatus;

  @Schema(description = "작성자 메일 주소")
  private String email;

  @Schema(description = "생성 날짜")
  private LocalDateTime createDate;

  @Schema(description = "생성자 id")
  private String createAdminAccountId;

  @Schema(description = "사용자 id")
  private String createAccountId;
}
