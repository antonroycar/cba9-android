package com.iotera.cba9handler.util;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Random;

public class Number {
    SecureRandom rand = new SecureRandom();

    public Number() {
    }

    public long GenerateRandomNumber() {
        long rnd = 0L;
        byte[] tmp = new byte[8];
        this.rand.nextBytes(tmp);

        for (byte i = 0; i < 8; ++i) {
            if (i == 7) {
                rnd += (long) (tmp[i] & 127) << 8 * i;
            } else {
                rnd += (long) tmp[i] << 8 * i;
            }
        }
        return rnd;
    }

    public long GeneratePrime() {
        return BigInteger.probablePrime(31, new Random()).longValue();
    }

    public long XpowYmodN(long x, long y, long N) {
        BigInteger bix = BigInteger.valueOf(x);
        BigInteger biy = BigInteger.valueOf(y);
        BigInteger biN = BigInteger.valueOf(N);
        BigInteger bires = bix.modPow(biy, biN);
        return bires.longValue();
    }
}
