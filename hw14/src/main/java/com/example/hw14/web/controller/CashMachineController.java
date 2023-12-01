package com.example.hw14.web.controller;

import com.example.hw14.cashmachine.machine.data.CashMachine;
import com.example.hw14.cashmachine.machine.data.MoneyBox;
import com.example.hw14.cashmachine.machine.service.CashMachineService;
import com.example.hw14.web.auth.Token;
import com.example.hw14.web.auth.TokenService;
import com.example.hw14.web.dto.CardDto;
import com.example.hw14.web.dto.LoginDto;
import com.example.hw14.web.dto.NotesDto;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@Controller
@RequestMapping("/atm")
public class CashMachineController {
    private final CashMachine cashMachine;
    private final CashMachineService cashMachineService;
    private final TokenService tokenService;

    @Resource(name = "sessionScopedToken")
    private Token token;

    public CashMachineController(CashMachineService cashMachineService, TokenService tokenService) {
        var moneyBox = new MoneyBox();
        cashMachine = new CashMachine(moneyBox);

        this.tokenService = tokenService;
        this.cashMachineService = cashMachineService;
    }

    @GetMapping
    public String index(@ModelAttribute("dto") LoginDto dto) {
        return "index";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute("dto") LoginDto dto) {
        if (tokenService.checkAuthenticationData(dto)) {
            tokenService.setAuthenticationData(token, dto.number(), dto.pin());
            return "redirect:/atm/homepage";
        }
        return "redirect:/atm/card/new";
    }

    @GetMapping("/logout")
    public String logout() {
        tokenService.logout(token);
        return "redirect:/atm";
    }

    @GetMapping("/homepage")
    public String homepage() {
        return "homepage";
    }

    @GetMapping("/card/new")
    public String newCard(@ModelAttribute("card") CardDto card) {
        return "new-card";
    }

    @PostMapping("/card/create")
    public String createCard(@ModelAttribute("card") CardDto card) {
        cashMachineService.createCard(card.number(), card.accountId(), card.pinCode());
        tokenService.setAuthenticationData(token, card.number(), card.pinCode());
        return "redirect:/atm/homepage";
    }

    @GetMapping("get-money")
    public String getMoneyPage() {
        return "get-money";
    }

    @PostMapping("get-money")
    public String getMoney(@RequestParam("amount") String amount) {
        cashMachineService.getMoney(cashMachine, token.getCardNumber(), token.getPinCode(), new BigDecimal(amount));
        return "success-operation-page";
    }

    @GetMapping("put-money")
    public String putMoneyPage(@ModelAttribute("notes") NotesDto notes) {
        return "put-money";
    }

    @PostMapping("put-money")
    public String putMoney(@ModelAttribute("notes") NotesDto notes) {
        cashMachineService.putMoney(cashMachine, token.getCardNumber(), token.getPinCode(), notes.toList());
        return "success-operation-page";
    }

    @GetMapping("check-balance")
    public String checkBalance(Model model) {
        BigDecimal balance = cashMachineService.checkBalance(cashMachine, token.getCardNumber(), token.getPinCode());
        model.addAttribute("balance", balance);
        return "check-balance";
    }

    @GetMapping("change-pin")
    public String changePinPage() {
        return "change-pin";
    }

    @PostMapping("change-pin")
    public String changePin(@RequestParam("newPin") String newPin) {
        cashMachineService.changePin(token.getCardNumber(), token.getPinCode(), newPin);
        tokenService.setAuthenticationData(token, token.getCardNumber(), newPin);
        return "success-operation-page";
    }
}
