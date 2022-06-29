package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.useraccount.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("lrc_project_user_account")
public class UserAccount {

    @MongoId(value = FieldType.STRING, targetType = FieldType.STRING)
    private String id;
    private String projectId;               //프로젝트 코드
    private String userAccountId;           //회원 아이디
    private String userType;                //마스터/담당자구분
}
