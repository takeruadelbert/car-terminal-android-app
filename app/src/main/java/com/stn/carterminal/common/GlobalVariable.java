package com.stn.carterminal.common;

import android.app.Application;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GlobalVariable extends Application {
    private String host;
    private String username;
    private String password;
}