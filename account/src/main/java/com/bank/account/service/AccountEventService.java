package com.bank.account.service;

import com.bank.account.dto.AccountDto;
import com.bank.account.kafka.AccountProducer;
import com.bank.account.util.AccountTopic;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AccountEventService {
    private final AccountProducer accountProducer;

    public void messageToKafka(AccountTopic accountTopic, AccountDto s) {
        accountProducer.send(accountTopic, s);
    }
}
