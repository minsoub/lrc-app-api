package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.model.listener;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.reviewestimate.model.entity.ReviewEstimate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class ReviewEstimateModelListener extends AbstractMongoEventListener<ReviewEstimate> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<ReviewEstimate> event) {
            if(event.getSource().getId() == null) {
                event.getSource().setId(UUID.randomUUID().toString());
            }
    }
}
