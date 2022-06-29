package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.listener;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.projectinfo.model.entity.ProjectInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class ProjectInfoModelListener extends AbstractMongoEventListener<ProjectInfo> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<ProjectInfo> event) {
            if(event.getSource().getId() == null) {
                event.getSource().setId(UUID.randomUUID().toString());
            }
    }
}
