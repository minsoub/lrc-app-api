package com.bithumbsystems.lrc.management.api.faq.content.mapper;

import com.bithumbsystems.lrc.management.api.faq.content.model.response.FaqContentResponse;
import com.bithumbsystems.persistence.mongodb.faq.content.model.entity.FaqContent;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface FaqContentMapper {

    FaqContentMapper INSTANCE = Mappers.getMapper(FaqContentMapper.class);

    FaqContentResponse faqContentRespone(FaqContent faqContent);
}
