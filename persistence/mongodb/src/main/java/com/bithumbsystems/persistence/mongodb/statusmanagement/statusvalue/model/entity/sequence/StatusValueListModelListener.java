package com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.model.entity.sequence;

import com.bithumbsystems.persistence.mongodb.common.service.ISequenceGeneratorService;
import com.bithumbsystems.persistence.mongodb.statusmanagement.statusvalue.model.entity.StatusValueList;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;

@Slf4j
@Component
public class StatusValueListModelListener extends AbstractMongoEventListener<StatusValueList> {

    private ISequenceGeneratorService sequenceGenerator;

    @Autowired
    public StatusValueListModelListener(ISequenceGeneratorService sequenceGenerator) { this.sequenceGenerator = sequenceGenerator; }

    @Override
    public void onBeforeConvert(BeforeConvertEvent<StatusValueList> event) {
        try {
            if(event.getSource().getCode() == null) {
                //String code = "SVL" + String.format("%02d", sequenceGenerator.generateSequence(StatusValueList.SEQUENCE_NAME));
                String code = "SVL" + sequenceGenerator.generateSequence(StatusValueList.SEQUENCE_NAME);
                event.getSource().setCode(code);
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("Error:{}", e.getMessage());
        }

    }


}
