package com.stn.carterminal.network.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Login {
    private String username;
    private String password;
}
