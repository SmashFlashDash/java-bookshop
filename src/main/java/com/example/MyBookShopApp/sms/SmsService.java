package com.example.MyBookShopApp.sms;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final SmsCodeRepository repository; // репозиторий для генеируемых отправлямых в sms кодов
    @Value("${twiloo.ACCOUNT_SID}")
    private String ACCOUNT_SID;
    @Value("${twiloo.AUTH_TOKEN}")
    private String AUTH_TOKEN;
    @Value("${twilio.TWILIO_NUMBER}")
    private String TWILIO_NUMBER;
    private final SmsCodeRepository smsCodeRepository;

    public String sendSecretCodeSms(String concat) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        String formattedContact = concat.replaceAll("[()-]", "");   // отчистить номер от символов
        String generatedCode = generateCode();
        Message.creator(
                new PhoneNumber(formattedContact),
                new PhoneNumber(TWILIO_NUMBER),
                "Your secret code is: " + generatedCode
        ).create();
        return generatedCode;
    }

    public void saveNewCode(SmsCode smsCode) {
        if (smsCodeRepository.findByCode(smsCode.getCode()) == null) {
            smsCodeRepository.save(smsCode);
        }
    }

    public Boolean verifyCode(String code) {
        SmsCode smsCode = smsCodeRepository.findByCode(code);
        return smsCode != null && smsCode.isExpired();
    }

    private String generateCode() {
        //nnn nnn - pattern
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        while (sb.length() < 6) {
            sb.append(random.nextInt(9));
        }
        sb.insert(3, " ");
        return sb.toString();
    }
}
