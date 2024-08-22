package com.example.springOAuth.service.Email;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class SmtpConfigModel {

    private String password;
    private String username;
    private String host;
    private int port;
    private boolean auth;
    private String debug;
    private boolean starttlsEnable;
    private String trust;
    private String protocol;
    private boolean enableSSl;

}
