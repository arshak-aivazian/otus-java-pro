package com.example.hw14.web.auth;

import com.example.hw14.cashmachine.bank.dao.CardsDao;
import com.example.hw14.cashmachine.bank.data.Card;
import com.example.hw14.web.dto.LoginDto;
import com.example.hw14.web.utils.HexFormatUtils;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {
    private final CardsDao cardsDao;

    public TokenServiceImpl(CardsDao cardsDao) {
        this.cardsDao = cardsDao;
    }

    @Override
    public boolean checkAuthenticationData(LoginDto loginDto) {
        Card card = cardsDao.getCardByNumber(loginDto.number());
        String hashPassword = HexFormatUtils.getHash(loginDto.pin());
        return card != null && card.getPinCode().equals(hashPassword);
    }

    @Override
    public void setAuthenticationData(Token token, String number, String pin) {
        token.setCardNumber(number);
        token.setPinCode(pin);
    }

    @Override
    public void logout(Token token) {
        token.setCardNumber(null);
        token.setPinCode(null);
    }
}
