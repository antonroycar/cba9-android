package com.iotera.cba9handler.service;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.iotera.cba9handler.command.BVCommand;
import com.iotera.cba9handler.util.HexUtil;

import java.util.Objects;

import tp.xmaihh.serialport.SerialHelper;
import tp.xmaihh.serialport.bean.ComBean;


public class BVTTYService extends SerialBV {
    public static SerialHelper serialHelper;
    String publicPortName;
    public static SharedPreferences sh;
    private static final String balance_key = "com.iotera.cba9handler.CASH_BALANCE";

    @Override
    public String openSerialPort(String portName, int baudRate) {
        publicPortName = portName;
        serialHelper = new SerialHelper(portName, baudRate) {
            @Override
            protected void onDataReceived(final ComBean comBean) {
                byte[] received = comBean.bRec;
                String hexReceived = HexUtil.byteArraytoHexString(received);
                String response = dataHandler(hexReceived);
                Log.i("BV", response);
            }
        };
        serialHelper.setPort(portName);
            serialHelper.setBaudRate(baudRate);
        serialHelper.setDataBits(8);
        serialHelper.setStopBits(2);
        try {
            serialHelper.open();
            Log.i("BV", "[NATIVE] Serial Port BV Open : " + portName);
        } catch (Exception e) {
            Log.i("BV", "[NATIVE] Opening Port Error : " + e);
            return "open port error : " + e;
        }

        if (serialHelper.isOpen()) {
            Log.i("BV","open port success");
            return "open port success";
        } else {
            Log.i("BV","open port failed");
            return "open port failed";
        }
    }

    @Override
    public String closePort() {
        try {
            serialHelper.close();
        } catch (Exception e) {
            return "close port error : " + e;
        }
        return "close port success";
    }

    @Override
    public void disableBV() {
        if (serialHelper.isOpen()) {
            BVCommand.disable();
        }
    }

    @Override
    public void connectBV() {
        if (serialHelper.isOpen()) {
            Handler loopHandler = new Handler(Looper.getMainLooper());
            Runnable sendDataRunnable = new Runnable() {
                @Override
                public void run() {
                    BVCommand.poll();
                    loopHandler.postDelayed(this, 500);
                }
            };
            loopHandler.post(sendDataRunnable);
        }
    }

    @Override
    public void enableBV() {
        if (serialHelper.isOpen()) {
            BVCommand.enable();
        }
    }

    private String dataHandler(String data) {
        String response = null;
        if (data.length() >= 4){
            String msgLengthStr = data.substring(0,4);
            String msg = msgLengthStr.substring(0, 2);
            switch (msg) {
                case "80":
                    String power_on = msgLengthStr.substring(2,4);
                    switch (power_on) {
                        case "8F":
                            response = "POWER ON";
                            break;
                    }
                    break;
                case "10":
                    response = "Stacking";
                    break;
                case "81":
                    String bill_stacked = msgLengthStr.substring(2, 4);
                    Integer cash = null;
                    switch (bill_stacked) {
                        case "40":
                            response = "IDR 1000 Accepted";
                            cash = 1000;
                            break;
                        case "41":
                            response = "IDR 2000 Accepted";
                            cash = 2000;
                            break;
                        case "42":
                            response = "IDR 5000 Accepted";
                            cash = 5000;
                            break;
                        case "43":
                            response = "IDR 10000 Accepted";
                            cash = 10000;
                            break;
                        case "44":
                            response = "IDR 20000 Accepted";
                            cash = 20000;
                            break;
                        case "45":
                            response = "IDR 50000 Accepted";
                            cash = 50000;
                            break;
                        case "46":
                            response = "IDR 100000 Accepted";
                            cash = 100000;
                            break;
                    }
                    break;
                case "11":
                    response = "Rejected";
                    break;
                case "29":
                    String failed = msgLengthStr.substring(2, 4);
                    switch (failed){
                        case "2F":
                            response = "Failed";
                            break;
                    }
                    break;
            }
        }
        return response;
    }
}
