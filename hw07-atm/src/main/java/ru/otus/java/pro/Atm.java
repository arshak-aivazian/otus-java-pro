package ru.otus.java.pro;

import java.util.List;

public class Atm implements CashTransactionHandler, BalanceAware {
    private final List<CashCell> cashCells;

    public Atm() {
        cashCells = List.of(
                new CashCell(Denomination.RUB_200),
                new CashCell(Denomination.RUB_500),
                new CashCell(Denomination.RUB_1000),
                new CashCell(Denomination.RUB_2000));
    }

    @Override
    public void showBalance() {
        int balance = cashCells.stream().mapToInt(CashCell::getCashAmount).sum();
        System.out.println("Остаток денежных средств в банкомате - " + balance);
    }

    @Override
    public void putCash(List<Banknote> banknotes) {
        for (Banknote banknote : banknotes) {
            Denomination denomination = Denomination.find(banknote.value(), banknote.currency());
            if (denomination == null) {
                System.out.println("Купюра неизвестного номинала - " + banknote);
            } else {
                cashCells.stream().filter(it -> it.getDenomination() == denomination)
                        .findFirst()
                        .ifPresentOrElse(
                                it -> it.addBanknote(banknote),
                                () -> System.out.println("Банкомат не принимает данную купюру - " + banknote)
                        );
            }
        }
    }

    @Override
    public List<Banknote> giveCash(int cashAmount, Currency currency) {
        CashCell cashCell = cashCells.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getDenomination().getAmount(), c1.getDenomination().getAmount()))
                .filter(cell -> isSuitableCell(cell, cashAmount, currency))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("В данный момент мы не можем выдать " + cashAmount + currency.name()));

        int banknoteCount = cashCell.getCashAmount() / cashAmount;

        List<Banknote> result = cashCell.pollBanknotes(banknoteCount);

        return result;
    }

    private boolean isSuitableCell(CashCell cell, int requiredCashAmount, Currency requiredCurrency) {
        return cell.getDenomination().getCurrency() == requiredCurrency
                && cell.getCashAmount() % requiredCashAmount == 0
                && cell.getCashAmount() / requiredCashAmount >= 1;
    }
}
