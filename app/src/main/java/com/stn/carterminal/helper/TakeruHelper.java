package com.stn.carterminal.helper;

import com.stn.carterminal.constant.Constant;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class TakeruHelper {
    public static boolean validateHost(String inputHost) {
        if (inputHost.equals(Constant.LOCALHOST_VALUE)) {
            return true;
        }
        boolean isIPv4;
        try {
            final InetAddress inet = InetAddress.getByName(inputHost);
            isIPv4 = inet.getHostAddress().equals(inputHost) && inet instanceof Inet4Address;
        } catch (final UnknownHostException e) {
            isIPv4 = false;
        }
        return isIPv4;
    }
}
