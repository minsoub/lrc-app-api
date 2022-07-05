package com.bithumbsystems.persistence.mongodb.audit.model.entity;

import com.bithumbsystems.persistence.mongodb.audit.model.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.Set;

@Document(collection = "audit_log")
@AllArgsConstructor
@Data
@Builder
public class AuditLog {

  @MongoId
  private String id;
  private String email;
  private String ip;
  private String menuId;
  private String menuName;
  private String programId;
  private String programName;
  private String method;
  private String crud;
  private String uri;
  private String path;
  private String queryParams;
  private String parameter;
  private String mySiteId;
  private String siteId;
  private String siteName;
  private RoleType roleType;
  private Set<String> roles;
  private String referer;
  private String device;
  private String message;
  private LocalDateTime createDate;
}
