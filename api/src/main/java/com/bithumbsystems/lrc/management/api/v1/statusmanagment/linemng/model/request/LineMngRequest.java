package com.bithumbsystems.lrc.management.api.v1.statusmanagment.linemng.model.request;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.enums.LineType;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Line mng request.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "계열 관리")
public class LineMngRequest {

  @Schema(description = "계열관리 코드", pattern = "xxxxxxxx-xxxx-4xxx-xxxx-xxxxxxxxxxxx")
  @Pattern(regexp = "^\\w{8}-\\w{4}-4\\w{3}-\\w{4}-\\w{12}", message = "uuid 형식만 가능. xxxxxxxx-xxxx-4xxx-xxxx-xxxxxxxxxxxx")
  private String id;

  @Schema(description = "계열관리 명", required = true)
  @NotBlank(groups = {OnCreate.class, OnUpdate.class}, message = "필수 항목")
  private String name;

  @Schema(description = "계열관리 타입 (생성일때만 사용)", required = true, allowableValues = {"BUSINESS", "NETWORK"})
  @NotNull(groups = OnCreate.class, message = "필수 항목")
  @Pattern(groups = OnCreate.class, regexp = "^BUSINESS$|^NETWORK$", message = "BUSINESS/NETWORK 만 지정 가능")
  // LineType Enum type을 사용하면 Json Parsing시 Exception 발생. javax.validation Exception을 공통적으로 발생시키기 위해 String type 사용
  private String type;

  @Schema(description = "사용여부", required = true, allowableValues = {"true", "false"})
  @NotNull(groups = {OnCreate.class, OnUpdate.class}, message = "필수 항목")
  @Pattern(groups = {OnCreate.class, OnUpdate.class}, regexp = "^true$|^false$", message = "true/false 만 지정 가능")
  // Boolean type을 쓰면 Json Parsing시 Exception 발생. javax.validation Exception을 공통적으로 발생시키기 위해 String type 사용
  private String useYn;

  @Schema(description = "정렬 순서", maximum = "999", defaultValue = "999")
  @Max(groups = {OnCreate.class, OnUpdate.class}, value = 999, message = "999 이하만 가능")
  @PositiveOrZero(groups = {OnCreate.class, OnUpdate.class}, message = "0 또는 양수만 가능")
  private Integer orderNo;

  @Schema(description = "상위계열코드 (생성일때만 사용)", pattern = "xxxxxxxx-xxxx-4xxx-xxxx-xxxxxxxxxxxx")
  @Pattern(groups = OnCreate.class, regexp = "^\\w{8}-\\w{4}-4\\w{3}-\\w{4}-\\w{12}", message = "uuid 형식만 가능. xxxxxxxx-xxxx-4xxx-xxxx-xxxxxxxxxxxx")
  private String parentId;

  /**
   * The interface On create.
   */
  public interface OnCreate {}

  /**
   * The interface On update.
   */
  public interface OnUpdate {}
}
