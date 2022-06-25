package com.bithumbsystems.lrc.management.api.core.config.resolver;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Account {
  private final String accountId;

  private final Set<String> roles;

  private final String email;
}
