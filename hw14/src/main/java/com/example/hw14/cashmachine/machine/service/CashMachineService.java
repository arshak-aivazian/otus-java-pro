package com.example.hw14.cashmachine.machine.service;

import com.example.hw14.cashmachine.bank.data.Card;
import com.example.hw14.cashmachine.machine.data.CashMachine;

import java.math.BigDecimal;
import java.util.List;

public interface CashMachineService {
    List<Integer> getMoney(CashMachine machine, String cardNum, String pin, BigDecimal amount);

    BigDecimal putMoney(CashMachine machine, String cardNum, String pin, List<Integer> notes);

    BigDecimal checkBalance(CashMachine machine, String cardNum, String pin);

    boolean changePin(String cardNum, String oldPin, String newPin);

    Card createCard(String number, Long accountId, String pin);
}
