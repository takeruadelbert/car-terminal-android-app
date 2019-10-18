package com.stn.carterminal.helper;

import java.util.regex.Pattern;

public class TakeruHelper {
    public static boolean validateHost(String inputHost) {
        Pattern pattern = Pattern.compile("^"
                + "(((?!-)[A-Za-z0-9-]{1,63}(?<!-)\\.)+[A-Za-z]{2,6}" // Domain name
                + "|"
                + "localhost" // localhost
                + "|"
                + "(([0-9]{1,3}\\.){3})[0-9]{1,3})" // Ip
                + ":"
                + "[0-9]{1,5}$"); // Port
        return pattern.matcher(inputHost).matches();
    }
}
