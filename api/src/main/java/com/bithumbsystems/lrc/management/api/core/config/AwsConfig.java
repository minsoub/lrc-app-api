package com.bithumbsystems.lrc.management.api.core.config;

import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import javax.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.kms.KmsAsyncClient;
import software.amazon.awssdk.services.s3.S3AsyncClient;
import software.amazon.awssdk.services.ses.SesClient;

import java.net.URI;

@Slf4j
@Getter
@Setter
@Configuration
@Profile("dev|prod|eks-dev|qa")
public class AwsConfig {

  private final AwsProperties awsProperties;
  @Value("${cloud.aws.credentials.profile-name}")
  private String profileName;
  private KmsAsyncClient kmsAsyncClient;
  @Value("${spring.profiles.active:}")
  private String activeProfiles;

  public AwsConfig(AwsProperties awsProperties) {
    this.awsProperties = awsProperties;
  }

  @Bean
  public S3AsyncClient s3client() {
    return S3AsyncClient.builder()
            //.endpointOverride(URI.create("https://s3.ap-northeast-2.amazonaws.com"))
        .region(Region.of(awsProperties.getRegion()))
        .build();
  }

  @Bean
  public SesClient sesClient() {
    return SesClient.builder()
            .endpointOverride(URI.create(awsProperties.getSesEndPoint())) // "https://ses.ap-northeast-2.amazonaws.com"))
            .region(Region.of(awsProperties.getRegion()))
            .build();
  }

  @PostConstruct
  public void init() {
    kmsAsyncClient = KmsAsyncClient.builder()
            .endpointOverride(URI.create(awsProperties.getKmsEndPoint())) // "https://kms.ap-northeast-2.amazonaws.com"))
        .region(Region.of(awsProperties.getRegion()))
        .build();
  }
}

