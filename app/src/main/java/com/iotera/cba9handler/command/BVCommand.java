package com.iotera.cba9handler.command;

import android.util.Log;

import com.iotera.cba9handler.service.BVTTYService;
import com.iotera.cba9handler.util.HexUtil;
import com.iotera.cba9handler.util.Keys;
import com.iotera.cba9handler.util.NumberUtil;
import com.iotera.cba9handler.util.SerialUtil;


public class BVCommand extends BVTTYService {
    private static int sequenceFlag = 1;
    static Keys key = new Keys();


    public static String BuildMessage(String data) {
        String STX = "7F";
        int Address = 0;
        int seqId = (sequenceFlag << 7) | Address;
        sequenceFlag = 1 - sequenceFlag;
        String sequenceId = String.format("%02X", seqId);
        byte[] byteData = HexUtil.hexStringToByteArray(data);
        int length = byteData.length;
        String strLength = String.format("%02X", length);
        String msg = sequenceId + strLength + data;
        byte[] bytes = HexUtil.hexStringToByteArray(msg);
        String crcHexString = SerialUtil.calculateCRC16(bytes);
        return STX + msg + crcHexString;
    }

    public static void enable() {
        String msg = BuildMessage("0A");
        serialHelper.sendHex(msg);
        Log.i("[BV - CBA9]", "BV Enabled");
        ;
    }

    public static void poll() {
        String msg = BuildMessage("07");
        serialHelper.sendHex(msg);
    }

    public static void disable() {
        String msg = BuildMessage("09");
        serialHelper.sendHex(msg);
        Log.i("[BV - CBA9]","BV Disabled");
    }
}
