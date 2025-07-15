package com.bank.transfer.services;



import com.bank.transfer.DTO.IncomingTransferDto;


public interface TransferService {


    void processTransfer(IncomingTransferDto incomingTransferDto);


}
