package com.iotera.cba9handler.service;

public abstract class SerialBV {
    abstract String openSerialPort(String portName, int baudRate);

    abstract String closePort();

    abstract void disableBV();

    abstract void connectBV();

    abstract void enableBV();
}
