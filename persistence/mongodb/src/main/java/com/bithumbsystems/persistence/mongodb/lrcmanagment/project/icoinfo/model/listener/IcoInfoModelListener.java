package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.model.listener;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.icoinfo.model.IcoInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class IcoInfoModelListener extends AbstractMongoEventListener<IcoInfo> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<IcoInfo> event) {
            if(event.getSource().getId() == null) {
                event.getSource().setId(UUID.randomUUID().toString());
            }
    }
}
