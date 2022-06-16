package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.mapper;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.model.request.SubmittedDocumentRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.model.response.SubmittedDocumentResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.model.SubmittedDocument;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubmittedDocumentMapper {

    SubmittedDocumentMapper INSTANCE = Mappers.getMapper(SubmittedDocumentMapper.class);

    SubmittedDocumentResponse submittedDocumentResponse(SubmittedDocument submittedDocument);

    SubmittedDocument submittedDocumentResponseToRequest(SubmittedDocument submittedDocument);

    SubmittedDocument requestToSubmittedDocument(SubmittedDocumentRequest submittedDocument);
}
