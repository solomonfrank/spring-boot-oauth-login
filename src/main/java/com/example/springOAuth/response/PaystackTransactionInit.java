package com.example.springOAuth.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PaystackTransactionInit {

    private boolean status;
    private String message;

    private MeteData data = new MeteData();

    @Data
    public static class MeteData {

        private String authorization_url;
        private String access_code;
        private String reference;

    }

}
