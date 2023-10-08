package ru.otus.java.pro;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Atm atm = new Atm();
        atm.putCash(List.of(new Banknote("1", 200, Currency.RUB)));
        atm.showBalance();
        atm.giveCash(200, Currency.RUB);
        atm.showBalance();
    }
}
