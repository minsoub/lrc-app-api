package com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.model.listener;

import com.bithumbsystems.persistence.mongodb.lrcmanagment.project.marketingquantity.model.entity.MarketingQuantity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class MarketingQuantityModelListener extends AbstractMongoEventListener<MarketingQuantity> {
    @Override
    public void onBeforeConvert(BeforeConvertEvent<MarketingQuantity> event) {
            if(event.getSource().getId() == null) {
                event.getSource().setId(UUID.randomUUID().toString());
            }
    }
}
