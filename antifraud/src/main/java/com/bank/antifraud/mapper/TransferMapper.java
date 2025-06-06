package com.bank.antifraud.mapper;

import com.bank.antifraud.entities.TransferChecked;

public interface TransferMapper {
    Object eventToDto(TransferChecked transfer);
    Object updateToDto(TransferChecked transfer);
    Object eventCleanToDto(TransferChecked transfer);
}
