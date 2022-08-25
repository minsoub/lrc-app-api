package com.bithumbsystems.lrc.management.api.core.util.message;

import com.bithumbsystems.lrc.management.api.core.model.enums.MailForm;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.io.IOException;

@Service
public interface MessageService {

  void send(final MailSenderInfo mailSenderInfo) throws MessagingException, IOException;

  void sendMail(String emailAddress, MailForm mailForm);
}