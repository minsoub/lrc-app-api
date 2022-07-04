package com.bithumbsystems.lrc.management.api.v1.audit.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class AuditLogSearchRequest {

  private final String searchText;
  private final String fromDate;
  private final String toDate;
}
