package com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.mapper;

import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.request.SubmittedDocumentFileRequest;
import com.bithumbsystems.lrc.management.api.v1.lrcmanagment.submitteddocument.file.model.response.SubmittedDocumentFileResponse;
import com.bithumbsystems.persistence.mongodb.lrcmanagment.submitteddocument.file.model.SubmittedDocumentFile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface SubmittedDocumentFileMapper {

    SubmittedDocumentFileMapper INSTANCE = Mappers.getMapper(SubmittedDocumentFileMapper.class);

    SubmittedDocumentFileResponse submittedDocumentFileResponse(SubmittedDocumentFile submittedDocumentFile);

    SubmittedDocumentFile submittedDocumentFileResponseToRequest(SubmittedDocumentFile submittedDocumentFile);

    SubmittedDocumentFile requestToSubmittedDocumentFile(SubmittedDocumentFileRequest submittedDocumentFileRequest);
}
