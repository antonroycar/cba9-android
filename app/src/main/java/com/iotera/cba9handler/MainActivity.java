package com.iotera.cba9handler;


import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.iotera.cba9handler.service.BVTTYService;

public class MainActivity extends AppCompatActivity {
    BVTTYService bvttyService = new BVTTYService();
    private TextView tvReceivedData;
    private TextView tvBalance;
    public static SharedPreferences sh;
    private static final long DOUBLE_BACK_PRESS_DELAY = 2000;
    private long backButtonPressedTime;
    private static final String balance_key = "com.iotera.cba9handler.CASH_BALANCE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvReceivedData = findViewById(R.id.tvReceivedData);
        tvBalance = findViewById(R.id.tvBalance);
        sh = getSharedPreferences(balance_key, MODE_PRIVATE);
        if (sh != null) {
            int balance = sh.getInt("balance", 0);
            String strBalance = "Saldo Rp. " + balance;
            tvBalance.setText(strBalance);
        }
        Button btnClrBalance = findViewById(R.id.btnClearBal);
        Button btnOpenSerialPort = findViewById(R.id.btnOpenSerialPort);
        Button btnEnable = findViewById(R.id.btnEnable);
        Button btnDisable = findViewById(R.id.btnDisable);
        Button btnCloseSerialPort = findViewById(R.id.btnClosePort);
        Button btnConnectBV = findViewById(R.id.btnConnectBV);
        btnOpenSerialPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String portName = "/dev/ttyS4";
                int baudRate = 9600;
                String response = bvttyService.openSerialPort(portName, baudRate);
                Toast.makeText(getBaseContext(), response, Toast.LENGTH_SHORT).show();
            }
        });
        btnCloseSerialPort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bvttyService.closePort();
            }
        });
        btnConnectBV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bvttyService.connectBV();
            }
        });
        btnEnable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bvttyService.enableBV();
            }
        });
        btnDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bvttyService.disableBV();
            }
        });
    }
}