package com.bithumbsystems.lrc.management.api.core.config.resolver;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@AllArgsConstructor
@Getter
public class Account {
  private final String accountId;

  private final Set<String> roles;

  private final String email;
  private final String userIp;
  private final String mySiteId;
}
