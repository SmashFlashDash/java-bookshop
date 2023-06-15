package com.example.MyBookShopApp.sms;

import org.springframework.data.jpa.repository.JpaRepository;

public interface SmsCodeRepository extends JpaRepository<SmsCode, Long> {
    SmsCode findByCode(String code);
}
