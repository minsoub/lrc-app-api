package com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.entity;

import com.bithumbsystems.persistence.mongodb.statusmanagement.linemng.model.enums.LineType;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.data.mongodb.core.mapping.MongoId;

import java.time.LocalDateTime;
import java.util.UUID;

@Document("lrc_line_mng")
@AllArgsConstructor
@Getter
@Setter
@Builder
@NoArgsConstructor
public class LineMng {

    @MongoId(value = FieldType.STRING, targetType = FieldType.STRING)
    private String id;
    private String name;        // 계열명
    private LineType type;        // 계열 타입
    private boolean useYn;
    private LocalDateTime createDate; //생성날짜
    private String createAdminAccountId;  //생성자 id

    private LocalDateTime updateDate; //수정날짜
    private String updateAdminAccountId; //수정자 id
}
