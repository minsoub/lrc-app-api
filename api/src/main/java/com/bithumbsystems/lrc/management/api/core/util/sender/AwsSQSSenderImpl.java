package com.bithumbsystems.lrc.management.api.core.util.sender;

import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.model.SendMessageRequest;
import com.amazonaws.services.sqs.model.SendMessageResult;
import com.bithumbsystems.lrc.management.api.core.config.properties.AwsProperties;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AwsSQSSenderImpl<T> implements AwsSQSSender<T> {
    private final AmazonSQSAsync amazonSQS;
    private final AwsProperties awsProperties;

    /**
     * Usage :
     *      private final AwsSQSSender<BucketUploadRequest> sqsSender;
     *
     *      // 전송 객체
     *      BucketUploadRequest.builder
     *
     *      sqsSender.sendMessage(bucketUploadRequest, UUID.randomUUID().toString());
     * @param bucketUploadRequest
     * @param groupId
     * @return
     */
    @Override
    public SendMessageResult sendMessage(T bucketUploadRequest, String groupId) {
        log.debug("AwsSQSSender Thread {}" , Thread.currentThread().getName());
        return amazonSQS.sendMessage(
                new SendMessageRequest(awsProperties.getSqsBucketavUrl(), new Gson().toJson(bucketUploadRequest))
                        .withMessageGroupId(groupId)
                        .withMessageDeduplicationId(UUID.randomUUID().toString())
        );
    }
}
