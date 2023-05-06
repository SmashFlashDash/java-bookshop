package com.example.MyBookShopApp.services;

import com.example.MyBookShopApp.data.repositories.BlackListJwtRepository;
import com.example.MyBookShopApp.data.token.BlackJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenService {
    private final BlackListJwtRepository jwtBlackList;
    
    @Autowired
    public TokenService(BlackListJwtRepository jwtBlackList) {
        this.jwtBlackList = jwtBlackList;
    }


    public void saveJwtBlackList(String value) {
        BlackJwt blackListToken = new BlackJwt();
        blackListToken.setToken(value);
        jwtBlackList.save(blackListToken);
    }

    public boolean isJwtInBlackList(String value) {
        BlackJwt blackJwt = jwtBlackList.findByToken(value);
        return blackJwt != null;
    }
}
