package com.bithumbsystems.lrc.management.api.core.util.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.InputStream;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MailSenderInfo {

  private String subject;

  private String bodyHTML;
  private String emailAddress;
  private InputStream is;
  private String fileName;

  private void setEmailAddress(String emailAddress) {
    this.emailAddress = emailAddress.trim();
  }
}