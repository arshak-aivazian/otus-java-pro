package ru.otus.java.pro;

import java.util.List;

public interface CashTransactionHandler {
    void putCash(List<Banknote> banknotes);

    List<Banknote> giveCash(int cashAmount, Currency currency);
}
