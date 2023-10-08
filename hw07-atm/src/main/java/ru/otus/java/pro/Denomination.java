package ru.otus.java.pro;

import lombok.Getter;

import java.util.Arrays;

import static ru.otus.java.pro.Currency.RUB;

@Getter
public enum Denomination {
    RUB_200(200, RUB),
    RUB_500(500, RUB),
    RUB_1000(1000, RUB),
    RUB_2000(2000, RUB);

    Denomination(int amount, Currency currency) {
        this.amount = amount;
        this.currency = currency;
    }

    private int amount;
    private Currency currency;

    public static Denomination find(int amount, Currency currency) {
        return Arrays.stream(values())
                .filter(it -> it.getAmount() == amount && it.getCurrency() == currency)
                .findFirst()
                .orElse(null);
    }
}
