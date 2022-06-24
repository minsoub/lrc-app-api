package com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.listener;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class HistoryModelListener extends AbstractMongoEventListener<History> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<History> event) {
            if(event.getSource().getId() == null) {
                event.getSource().setId(UUID.randomUUID().toString());
            }
    }
}
