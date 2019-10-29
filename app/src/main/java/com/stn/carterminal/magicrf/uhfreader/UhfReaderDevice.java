package com.stn.carterminal.magicrf.uhfreader;

import com.stn.carterminal.uhf.reader.SerialPort;

public class UhfReaderDevice {
    private static UhfReaderDevice readerDevice;
    private static SerialPort devPower;

    public static UhfReaderDevice getInstance() {
        if (readerDevice == null) {
            readerDevice = new UhfReaderDevice();
        }
        return readerDevice;
    }

    public void powerOn() {
        devPower.psampoweron();
    }

    public void powerOff() {
        if (devPower != null) {
            devPower.psampoweroff();
            devPower = null;
        }
    }
}
