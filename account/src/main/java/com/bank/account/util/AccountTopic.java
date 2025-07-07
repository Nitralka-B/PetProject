package com.bank.account.util;

import lombok.Getter;

@Getter
public enum AccountTopic {
    CREATE("account.create"),
    UPDATE("account.update"),
    DELETE("account.delete"),
    GET("account.get");

    private final String topic;

    AccountTopic(String topic) {
        this.topic = topic;
    }

}

