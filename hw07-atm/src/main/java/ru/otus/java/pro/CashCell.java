package ru.otus.java.pro;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CashCell {
    private final Denomination denomination;
    private final List<Banknote> banknotes;

    public CashCell(Denomination denomination) {
        this.denomination = denomination;
        banknotes = new ArrayList<>();
    }

    public List<Banknote> pollBanknotes(int limit) {
        List<Banknote> result = banknotes.stream().limit(limit).collect(Collectors.toList());

        banknotes.removeAll(result);

        return result;
    }

    public void addBanknote(Banknote banknote) {
        banknotes.add(banknote);
    }

    public int getCashAmount() {
        return banknotes.stream().mapToInt(Banknote::value).sum();
    }
}
