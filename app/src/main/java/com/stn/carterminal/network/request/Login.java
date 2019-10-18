package com.stn.carterminal.network.request;

import lombok.Data;

@Data
public class Login {
    private String username;
    private String password;

    public Login(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
