package com.example.hw13.web.controller;

import com.example.hw13.cashmachine.bank.dao.AccountDao;
import com.example.hw13.cashmachine.bank.dao.CardsDao;
import com.example.hw13.cashmachine.bank.data.Account;
import com.example.hw13.cashmachine.bank.data.Card;
import com.example.hw13.cashmachine.bank.service.CardService;
import com.example.hw13.cashmachine.machine.data.CashMachine;
import com.example.hw13.cashmachine.machine.data.MoneyBox;
import com.example.hw13.cashmachine.machine.service.CashMachineService;
import com.example.hw13.web.dto.LoginDto;
import com.example.hw13.web.utils.HexFormatUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/atm")
public class CashMachineController {
    private final CashMachine cashMachine;
    private final CashMachineService cashMachineService;
    private final CardsDao cardsDao;
    private final AccountDao accountDao;
    private final CardService cardService;

    private Token token;

    public CashMachineController(CashMachineService cashMachineService, CardsDao cardsDao, CardService cardService, AccountDao accountDao) {
        var moneyBox = new MoneyBox();
        cashMachine = new CashMachine(moneyBox);

        this.cashMachineService = cashMachineService;
        this.cardsDao = cardsDao;
        this.accountDao = accountDao;
        this.cardService = cardService;
    }

    @GetMapping
    public String index(@ModelAttribute("dto") LoginDto dto) {
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("dto") LoginDto dto) {
        Card card = cardsDao.getCardByNumber(dto.number());
        String hash = HexFormatUtils.getHash(dto.pin());
        if (card != null && card.getPinCode().equals(hash)) {
            this.token = new Token(dto.number(), dto.pin());
            return "redirect:/atm/homepage";
        }
        return "redirect:/atm/card/new";
    }

    @GetMapping("/logout")
    public String logout() {
        token = null;
        return "redirect:/atm";
    }

    @GetMapping("/homepage")
    public String homepage() {
        return "homepage";
    }

    @GetMapping("/card/new")
    public String newCard(@ModelAttribute("card") Card card) {
        return "new-card";
    }

    @PostMapping("/card/create")
    public String createCard(@ModelAttribute("card") Card card) {
        Card newCard = cardService.createCard(card.getNumber(), card.getAccountId(), HexFormatUtils.getHash(card.getPinCode()));
        accountDao.saveAccount(new Account(card.getAccountId(), BigDecimal.valueOf(100_000_000)));
        if (newCard != null) {
            this.token = new Token(card.getNumber(), card.getPinCode());
            return "redirect:/atm/homepage";
        }
        throw new AtmRestException("Что-то пошло не так");
    }

    @GetMapping("get-money")
    public String getMoneyPage() {
        return "get-money";
    }

    @PostMapping("get-money")
    public String getMoney(@RequestParam("amount") String amount) {
        cashMachineService.getMoney(cashMachine, token.number(), token.pin, new BigDecimal(amount));
        return "success-operation-page";
    }

    @GetMapping("put-money")
    public String putMoneyPage() {
        return "put-money";
    }

    @PostMapping("put-money")
    public String putMoney(@RequestParam("count") Integer count, @RequestParam("note") Integer note) {
        List<Integer> notes = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            notes.add(note);
        }
        cashMachineService.putMoney(cashMachine, token.number(), token.pin(), notes);
        return "success-operation-page";
    }

    @GetMapping("check-balance")
    public String checkBalance(Model model) {
        BigDecimal balance = cashMachineService.checkBalance(cashMachine, token.number(), token.pin());
        model.addAttribute("balance", balance);
        return "check-balance";
    }

    @GetMapping("change-pin")
    public String changePinPage() {
        return "change-pin";
    }

    @PostMapping("change-pin")
    public String changePin(@RequestParam("newPin") String newPin) {
        cashMachineService.changePin(token.number, token.pin, newPin);
        return "success-operation-page";
    }

    private record Token(String number, String pin) {
    }

    private static class AtmRestException extends RuntimeException {
        public AtmRestException(String message) {
            super(message);
        }
    }

}
