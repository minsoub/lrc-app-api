package com.bithumbsystems.lrc.management.api.core.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;

@ConfigurationPropertiesBinding
@ConfigurationProperties(prefix = "application")
@Getter
@Setter
public class ApplicationProperties {

  private String version;
  private String prefix;
  private String[] excludePrefixPath;
}
