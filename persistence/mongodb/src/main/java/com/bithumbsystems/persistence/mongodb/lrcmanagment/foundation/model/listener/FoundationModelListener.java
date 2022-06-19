package com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.model.listener;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.foundation.model.entity.Foundation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class FoundationModelListener extends AbstractMongoEventListener<Foundation> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<Foundation> event) {
            if(event.getSource().getId() == null) {
                event.getSource().setId(UUID.randomUUID().toString());
            }
    }
}
