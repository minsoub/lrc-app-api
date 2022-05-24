package com.bithumbsystems.lrc.management.api.v1.faq.category.mapper;

import com.bithumbsystems.lrc.management.api.v1.faq.category.model.request.FaqCategoryRequest;
import com.bithumbsystems.lrc.management.api.v1.faq.category.model.response.FaqCategoryResponse;
import com.bithumbsystems.persistence.mongodb.faq.category.model.entity.FaqCategory;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FaqCategoryMapper {

    FaqCategoryMapper INSTANCE = Mappers.getMapper(FaqCategoryMapper.class);

    FaqCategoryResponse faqCategoryResponse(FaqCategory faqCategory);

    FaqCategory faqCategoryRequestToFaqContent(FaqCategoryRequest faqCategoryRequest);
}
