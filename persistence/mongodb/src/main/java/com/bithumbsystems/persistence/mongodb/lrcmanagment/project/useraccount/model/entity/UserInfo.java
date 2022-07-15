package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("lrc_user_account")
public class UserInfo {
    @MongoId(value = FieldType.STRING, targetType = FieldType.STRING)
    private String id;
    private String email;
    private String password;
    private LocalDateTime lastLoginDate;
//    private String name;
//    private String phone;
//    private String snsId;
//    private String contactEmail;
    private String status;
    private String otpSecretKey;
    private LocalDateTime createDate;
    private String createAccountId;
    private LocalDateTime updateDate;
    private String updateAccountId;
}
