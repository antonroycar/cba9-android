package com.iotera.cba9handler.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;


public class NumberUtil {
    static Number rand = new Number();

    public static String CreateKey(long number) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        buffer.putLong(number);
        byte[] byteArray = buffer.array();
        StringBuilder hexString = new StringBuilder();
        for (byte b : byteArray) {
            hexString.append(String.format("%02X", b));
        }
        return hexString.toString();
    }


    public static boolean InitiateSSPHostKeys(Keys keys) {
        long swap = 0L;
        keys.Generator = rand.GeneratePrime();
        keys.Modulus = rand.GeneratePrime();
        if (keys.Generator < keys.Modulus) {
            swap = keys.Generator;
            keys.Generator = keys.Modulus;
            keys.Modulus = swap;
        }

        return CreateHostInterKey(keys);
    }

    private long GeneratePrime() {
        long tmp = rand.GenerateRandomNumber() % 2147483647L;
        if ((tmp & 1L) == 0L) {
            ++tmp;
        }

        if (this.MillerRabin(tmp, 5L) == 1) {
            return tmp;
        } else {
            do {
                tmp += 2L;
            } while (this.MillerRabin(tmp, 5L) == 0);

            return tmp;
        }
    }

    private int MillerRabin(long n, long trials) {
        long a = 0L;

        for (int i = 0; (long) i < trials; ++i) {
            a = rand.GenerateRandomNumber() % (n - 3L) + 2L;
            if (this.IsItPrime(n, a) == 0) {
                return 0;
            }
        }

        return 1;
    }

    private int IsItPrime(long n, long a) {
        long d = rand.XpowYmodN(a, n - 1L, n);
        return d == 1L ? 1 : 0;
    }


    private static boolean CreateHostInterKey(Keys keys) {
        if (keys.Generator != 0L && keys.Modulus != 0L) {
            keys.HostRandom = rand.GenerateRandomNumber() % 2147483647L;
            keys.HostInter = rand.XpowYmodN(keys.Generator, keys.HostRandom, keys.Modulus);
            return true;
        } else {
            return false;
        }
    }
}
