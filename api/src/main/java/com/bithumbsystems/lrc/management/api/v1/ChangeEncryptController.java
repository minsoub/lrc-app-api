package com.bithumbsystems.lrc.management.api.v1;

import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.persistence.mongodb.chat.repository.ChatMessageRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository.UserAccountRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository.UserInfoRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.repository.SubmittedDocumentFileRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.repository.SubmittedDocumentUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController("change")
@Slf4j
@RequiredArgsConstructor
@Profile("dev|prod|eks-dev|qa")
public class ChangeEncryptController {

  private final UserInfoRepository lrcUserAccountRepository;
  private final UserAccountRepository lrcProjectUserAccountRepository;
  private final ChatMessageRepository chatMessageRepository;
  private final SubmittedDocumentFileRepository submittedDocumentFileRepository;
  private final SubmittedDocumentUrlRepository submittedDocumentUrlRepository;
  private final AwsProperties awsProperties;

  @GetMapping("/info")
  @Transactional
  public Flux<Object> userInfo() {
    try {
      return getUserInfoFlux();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException();
    }
  }

  @GetMapping("/account")
  @Transactional
  public Flux<Object> userAccount() {
    try {
      return getUserAccountFlux();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException();
    }
  }

  @GetMapping("/file")
  @Transactional
  public Flux<Object> submittedDocumentFile() {
    try {
      return getSubmittedDocumentFileFlux();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException();
    }
  }

  @GetMapping("/url")
  @Transactional
  public Flux<Object> submittedDocumentUrl() {
    try {
      return getSubmittedDocumentUrlFlux();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException();
    }
  }

  @GetMapping("/chat")
  public Flux<Object> chatMessage() {
    try {
      return getChatMessageFlux();
    } catch (Exception e) {
      log.error(e.getMessage());
      throw new RuntimeException();
    }
  }

  public Flux<Object> getChatMessageFlux() {
    return chatMessageRepository.findAll()
        .flatMap(chatMessage -> {
          if(chatMessage.getEmail() != null) {
            var email = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), chatMessage.getEmail());
            if(!StringUtils.hasLength(email))  {
              return chatMessageRepository.delete(chatMessage);
            }
            chatMessage.setEmail(AES256Util.encryptAES(awsProperties.getKmsKey(), email, awsProperties.getSaltKey(), awsProperties.getIvKey()));
          } else {
            return chatMessageRepository.delete(chatMessage);
          }

          if(chatMessage.getContent() != null) {
            var contents = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), chatMessage.getContent());
            chatMessage.setContent(AES256Util.encryptAES(awsProperties.getKmsKey(), contents, awsProperties.getSaltKey(), awsProperties.getIvKey()));
            if(!StringUtils.hasLength(contents))  {
              return chatMessageRepository.delete(chatMessage);
            }
          } else {
            return chatMessageRepository.delete(chatMessage);
          }
          return chatMessageRepository.save(chatMessage);
        });
  }

  @Transactional
  public Flux<Object> getSubmittedDocumentUrlFlux() {
    return submittedDocumentUrlRepository.findAll()
        .filter(submittedDocumentUrl -> submittedDocumentUrl.getEmail() != null)
        .flatMap(submittedDocumentUrl -> {
          var email = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), submittedDocumentUrl.getEmail());
          if(!StringUtils.hasLength(email)) {
            return submittedDocumentUrlRepository.delete(submittedDocumentUrl);
          }
          submittedDocumentUrl.setEmail(AES256Util.encryptAES(awsProperties.getKmsKey(), email, awsProperties.getSaltKey(), awsProperties.getIvKey()));
          return submittedDocumentUrlRepository.save(submittedDocumentUrl);
        });
  }

  @Transactional
  public Flux<Object> getSubmittedDocumentFileFlux() {
    return submittedDocumentFileRepository.findAll()
        .filter(submittedDocumentFile -> submittedDocumentFile.getEmail() != null)
        .flatMap(submittedDocumentFile -> {
          var email = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), submittedDocumentFile.getEmail());
          if(!StringUtils.hasLength(email)) {
            return submittedDocumentFileRepository.delete(submittedDocumentFile);
          }
          submittedDocumentFile.setEmail(AES256Util.encryptAES(awsProperties.getKmsKey(), email, awsProperties.getSaltKey(), awsProperties.getIvKey()));
          return submittedDocumentFileRepository.save(submittedDocumentFile);
        });
  }

  @Transactional
  public Flux<Object> getUserAccountFlux() {
    return lrcProjectUserAccountRepository.findAll().flatMap(lrcProjectUserAccount -> {
      if(lrcProjectUserAccount.getContactEmail() != null) {
        var contactEmail = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), lrcProjectUserAccount.getContactEmail());
        if(!StringUtils.hasLength(contactEmail)) {
          return lrcProjectUserAccountRepository.delete(lrcProjectUserAccount);
        }
        lrcProjectUserAccount.setContactEmail(AES256Util.encryptAES(awsProperties.getKmsKey(), contactEmail, awsProperties.getSaltKey(), awsProperties.getIvKey()));
      }

      if(lrcProjectUserAccount.getName() != null) {
        var name = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), lrcProjectUserAccount.getName());
        if(!StringUtils.hasLength(name)) {
          return lrcProjectUserAccountRepository.delete(lrcProjectUserAccount);
        }
        lrcProjectUserAccount.setName(AES256Util.encryptAES(awsProperties.getKmsKey(), name, awsProperties.getSaltKey(), awsProperties.getIvKey()));
      }

      if(lrcProjectUserAccount.getPhone() != null) {
        var phone = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), lrcProjectUserAccount.getPhone());
        if(!StringUtils.hasLength(phone)) {
          return lrcProjectUserAccountRepository.delete(lrcProjectUserAccount);
        }
        lrcProjectUserAccount.setPhone(AES256Util.encryptAES(awsProperties.getKmsKey(), phone, awsProperties.getSaltKey(), awsProperties.getIvKey()));
      }

      if(lrcProjectUserAccount.getSnsId() != null) {
        var snsId = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), lrcProjectUserAccount.getSnsId());
        if(!StringUtils.hasLength(snsId)) {
          return lrcProjectUserAccountRepository.delete(lrcProjectUserAccount);
        }
        lrcProjectUserAccount.setSnsId(AES256Util.encryptAES(awsProperties.getKmsKey(), snsId, awsProperties.getSaltKey(), awsProperties.getIvKey()));
      }
      return lrcProjectUserAccountRepository.save(lrcProjectUserAccount);
    });
  }

  @Transactional
  public Flux<Object> getUserInfoFlux() {
    return lrcUserAccountRepository.findAll().flatMap(lrcUserAccount -> {
      var email = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), lrcUserAccount.getEmail());
      if(!StringUtils.hasLength(email)) return lrcUserAccountRepository.delete(lrcUserAccount);
      log.info(email);
      lrcUserAccount.setEmail(AES256Util.encryptAES(awsProperties.getKmsKey(), email, awsProperties.getSaltKey(), awsProperties.getIvKey()));
      var saveEmail = AES256Util.decryptAES(awsProperties.getKmsKey(), lrcUserAccount.getEmail());
      if(!email.equals(saveEmail)) throw new RuntimeException("fail");
      return lrcUserAccountRepository.save(lrcUserAccount);
    });
  }
}
