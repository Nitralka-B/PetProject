package com.bank.authorization.kafka.enumTopic;

import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public enum KafkaTopic {
    USER_CREATE,
    USER_DELETE,
    USER_UPDATE,
    USER_GET;

    private String topicName;

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
