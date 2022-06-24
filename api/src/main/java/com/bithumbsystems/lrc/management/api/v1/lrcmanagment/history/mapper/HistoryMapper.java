package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.mapper;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.request.HistoryRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.history.model.response.HistoryResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.history.model.entity.History;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HistoryMapper {

    HistoryMapper INSTANCE = Mappers.getMapper(HistoryMapper.class);

    HistoryResponse historyResponse(History history);

    History historyResponseToRequest(HistoryRequest historyRequest);
}
