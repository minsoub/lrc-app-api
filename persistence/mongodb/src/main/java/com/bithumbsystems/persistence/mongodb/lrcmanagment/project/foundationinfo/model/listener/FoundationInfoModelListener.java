package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.model.listener;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.foundationinfo.model.entity.FoundationInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Component
public class FoundationInfoModelListener extends AbstractMongoEventListener<FoundationInfo> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<FoundationInfo> event) {
            if(event.getSource().getId() == null) {
                event.getSource().setId(UUID.randomUUID().toString());
                event.getSource().setCreateDate(LocalDateTime.now());
            }
    }
}
