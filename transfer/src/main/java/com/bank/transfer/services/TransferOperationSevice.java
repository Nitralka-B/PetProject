package com.bank.transfer.services;

import com.bank.transfer.DTO.IncomingTransferDto;
import com.bank.transfer.ENUM.TransferType;

public interface TransferOperationSevice {
    Object saveTransfer(IncomingTransferDto dto);
    Object updateTransfer (Long id, IncomingTransferDto dto, TransferType type);

}
