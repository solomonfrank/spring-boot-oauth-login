package com.example.springOAuth.response;

import java.math.BigDecimal;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PaystackVerifyTransaction {

    private Boolean status;
    private String message;

    private MeteData data = new MeteData();

    @Data
    public static class MeteData {

        private long id;
        private String domain;
        private String reference;
        private BigDecimal amount;
        private String channel;
        private String currency;

        private int fee;

    }

}
