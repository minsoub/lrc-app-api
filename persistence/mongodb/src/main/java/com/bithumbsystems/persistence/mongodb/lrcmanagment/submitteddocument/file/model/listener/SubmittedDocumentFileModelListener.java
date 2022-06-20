package com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.model.listener;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.model.SubmittedDocumentFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class SubmittedDocumentFileModelListener extends AbstractMongoEventListener<SubmittedDocumentFile> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<SubmittedDocumentFile> event) {
            if(event.getSource().getId() == null) {
                event.getSource().setId(UUID.randomUUID().toString());
            }
    }
}
