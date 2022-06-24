package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.mapper;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.model.request.SubmittedDocumentUrlRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.url.model.response.SubmittedDocumentUrlResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.url.model.entity.SubmittedDocumentUrl;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubmittedDocumentUrlMapper {

    SubmittedDocumentUrlMapper INSTANCE = Mappers.getMapper(SubmittedDocumentUrlMapper.class);

    SubmittedDocumentUrlResponse submittedDocumentUrlResponse(SubmittedDocumentUrl submittedDocumentUrl);

    SubmittedDocumentUrl submittedDocumentUrlResponseToRequest(SubmittedDocumentUrl submittedDocumentUrl);

    SubmittedDocumentUrl requestToSubmittedDocumentUrl(SubmittedDocumentUrlRequest submittedDocumentUrlRequest);
}
