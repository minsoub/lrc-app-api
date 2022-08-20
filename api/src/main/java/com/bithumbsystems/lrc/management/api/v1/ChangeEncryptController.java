package com.bithumbsystems.lrc.management.api.v1;

import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.bithumbsystems.lrc.management.api.core.util.AES256Util;
import com.bithumbsystems.persistence.mongodb.chat.model.entity.ChatMessage;
import com.bithumbsystems.persistence.mongodb.chat.repository.ChatMessageRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserAccount;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity.UserInfo;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository.UserAccountRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.repository.UserInfoRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.model.entity.SubmittedDocumentFile;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.repository.SubmittedDocumentFileRepository;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.model.entity.SubmittedDocumentUrl;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.repository.SubmittedDocumentUrlRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController("change")
@Slf4j
@RequiredArgsConstructor
public class ChangeEncryptController {

//  private final UserInfoRepository lrcUserAccountRepository;
//  private final UserAccountRepository lrcProjectUserAccountRepository;
//  private final ChatMessageRepository chatMessageRepository;
//  private final SubmittedDocumentFileRepository submittedDocumentFileRepository;
//  private final SubmittedDocumentUrlRepository submittedDocumentUrlRepository;
//  private final AwsProperties awsProperties;
//
//  @GetMapping("/info")
//  @Transactional
//  public Flux<UserInfo> userInfo() {
//    try {
//      return getUserInfoFlux();
//    } catch (Exception e) {
//      log.error(e.getMessage());
//      throw new RuntimeException();
//    }
//  }
//
//  @GetMapping("/account")
//  @Transactional
//  public Flux<UserAccount> userAccount() {
//    try {
//      return getUserAccountFlux();
//    } catch (Exception e) {
//      log.error(e.getMessage());
//      throw new RuntimeException();
//    }
//  }
//
//  @GetMapping("/file")
//  @Transactional
//  public Flux<SubmittedDocumentFile> submittedDocumentFile() {
//    try {
//      return getSubmittedDocumentFileFlux();
//    } catch (Exception e) {
//      log.error(e.getMessage());
//      throw new RuntimeException();
//    }
//  }
//
//  @GetMapping("/url")
//  @Transactional
//  public Flux<SubmittedDocumentUrl> submittedDocumentUrl() {
//    try {
//      return getSubmittedDocumentUrlFlux();
//    } catch (Exception e) {
//      log.error(e.getMessage());
//      throw new RuntimeException();
//    }
//  }
//
//  @GetMapping("/chat")
//  public Flux<ChatMessage> chatMessage() {
//    try {
//      return getChatMessageFlux();
//    } catch (Exception e) {
//      log.error(e.getMessage());
//      throw new RuntimeException();
//    }
//  }
//
//  public Flux<ChatMessage> getChatMessageFlux() {
//    return chatMessageRepository.findAll()
//        .flatMap(chatMessage -> {
//          if(chatMessage.getEmail() != null) {
//            var email = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), chatMessage.getEmail());
//            chatMessage.setEmail(AES256Util.encryptAES(awsProperties.getKmsKey(), email, awsProperties.getSaltKey(), awsProperties.getIvKey()));
//          }
//
//          if(chatMessage.getContent() != null) {
//            var contents = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), chatMessage.getContent());
//            chatMessage.setContent(AES256Util.encryptAES(awsProperties.getKmsKey(), contents, awsProperties.getSaltKey(), awsProperties.getIvKey()));
//          }
//          return chatMessageRepository.save(chatMessage);
//        });
//  }
//
//  @Transactional
//  public Flux<SubmittedDocumentUrl> getSubmittedDocumentUrlFlux() {
//    return submittedDocumentUrlRepository.findAll()
//        .filter(submittedDocumentUrl -> submittedDocumentUrl.getEmail() != null)
//        .flatMap(submittedDocumentUrl -> {
//          var email = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), submittedDocumentUrl.getEmail());
//          submittedDocumentUrl.setEmail(AES256Util.encryptAES(awsProperties.getKmsKey(), email, awsProperties.getSaltKey(), awsProperties.getIvKey()));
//          return submittedDocumentUrlRepository.save(submittedDocumentUrl);
//        });
//  }
//
//  @Transactional
//  public Flux<SubmittedDocumentFile> getSubmittedDocumentFileFlux() {
//    return submittedDocumentFileRepository.findAll()
//        .filter(submittedDocumentFile -> submittedDocumentFile.getEmail() != null)
//        .flatMap(submittedDocumentFile -> {
//          var email = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), submittedDocumentFile.getEmail());
//          submittedDocumentFile.setEmail(AES256Util.encryptAES(awsProperties.getKmsKey(), email, awsProperties.getSaltKey(), awsProperties.getIvKey()));
//          return submittedDocumentFileRepository.save(submittedDocumentFile);
//        });
//  }
//
//  @Transactional
//  public Flux<UserAccount> getUserAccountFlux() {
//    return lrcProjectUserAccountRepository.findAll().flatMap(lrcProjectUserAccount -> {
//      if(lrcProjectUserAccount.getContactEmail() != null) {
//        var contactEmail = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), lrcProjectUserAccount.getContactEmail());
//        lrcProjectUserAccount.setContactEmail(AES256Util.encryptAES(awsProperties.getKmsKey(), contactEmail, awsProperties.getSaltKey(), awsProperties.getIvKey()));
//      }
//
//      if(lrcProjectUserAccount.getName() != null) {
//        var name = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), lrcProjectUserAccount.getName());
//        lrcProjectUserAccount.setName(AES256Util.encryptAES(awsProperties.getKmsKey(), name, awsProperties.getSaltKey(), awsProperties.getIvKey()));
//      }
//
//      if(lrcProjectUserAccount.getPhone() != null) {
//        var phone = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), lrcProjectUserAccount.getPhone());
//        lrcProjectUserAccount.setPhone(AES256Util.encryptAES(awsProperties.getKmsKey(), phone, awsProperties.getSaltKey(), awsProperties.getIvKey()));
//      }
//
//      if(lrcProjectUserAccount.getSnsId() != null) {
//        var snsId = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), lrcProjectUserAccount.getSnsId());
//        lrcProjectUserAccount.setSnsId(AES256Util.encryptAES(awsProperties.getKmsKey(), snsId, awsProperties.getSaltKey(), awsProperties.getIvKey()));
//      }
//      return lrcProjectUserAccountRepository.save(lrcProjectUserAccount);
//    });
//  }
//
//  @Transactional
//  public Flux<UserInfo> getUserInfoFlux() {
//    return lrcUserAccountRepository.findAll().flatMap(lrcUserAccount -> {
//      var email = AES256Util.decryptAESLegacy(awsProperties.getKmsKey(), lrcUserAccount.getEmail());
//      log.info(email);
//      lrcUserAccount.setEmail(AES256Util.encryptAES(awsProperties.getKmsKey(), email, awsProperties.getSaltKey(), awsProperties.getIvKey()));
//      var saveEmail = AES256Util.decryptAES(awsProperties.getKmsKey(), lrcUserAccount.getEmail());
//      if(!email.equals(saveEmail)) throw new RuntimeException("fail");
//      return lrcUserAccountRepository.save(lrcUserAccount);
//    });
//  }
}
