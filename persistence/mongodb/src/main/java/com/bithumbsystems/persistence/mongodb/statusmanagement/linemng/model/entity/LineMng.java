package com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.entity;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.enums.LineType;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

/**
 * The type Line mng.
 */
@Document("lrc_line_mng")
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class LineMng {

  @MongoId(value = FieldType.STRING, targetType = FieldType.STRING)
  private String id;
  private String name;                    //계열명
  private LineType type;                  //계열 타입
  private Boolean useYn;                  //사용여부
  private Boolean delYn;                  //삭제 여부
  private LocalDateTime createDate;       //생성날짜
  private String createAdminAccountId;    //생성자 id
  private LocalDateTime updateDate;       //수정날짜
  private String updateAdminAccountId;    //수정자 id
  private Integer orderNo;                //순서
  private String parentId;                //상위분류코드
  @ReadOnlyProperty
  private LineMng parentInfo;             // 상위 계열 정보
}
