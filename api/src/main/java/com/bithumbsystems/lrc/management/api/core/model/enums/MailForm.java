package com.bithumbsystems.lrc.management.api.core.model.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum MailForm {
  DEFAULT("subject", "mail/default.html"),
  KOR("[Bithumb 거래지원 및 문의] 커뮤니케이션 알림", "mail/mailform_kor.html"),
  EN("[Bithumb Listing] Communication Notifications", "mail/mailform_en.html");


  private final String subject;

  private final String path;
}
