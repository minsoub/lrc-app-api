package com.bithumbsystems.lrc.management.api.core.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
@Setter
public class MailProperties {
    @Value("${mail.logourl}")
    private String logoUrl;

    @Value("${mail.loginurl}")
    private String loginUrl;
}
