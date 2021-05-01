package me.proxima.kakao.emoticon.api;

public class EmoticonDecryptor {
    public static long hash(int i) {
        return ((long) i) & 4294967295L;
    }

    public static byte[] decryptBytes(byte[] data, String key, int i) {
        Crypto crypto = new Crypto();
        crypto.initialize(key);
        return crypto.decryptBytes(data, i);
    }

    public static byte[] decryptBytes(byte[] data, String key) {
        return decryptBytes(data, key, 1);
    }

    public static class Crypto {
        public int key1;
        public int key2;
        public int key3;

        public Crypto() {
            this.key1 = 301989938;
            this.key2 = 623357073;
            this.key3 = -2004086252;
        }

        public Crypto initialize(String key) {
            int length = key.getBytes().length;
            byte[] keyBytes = new byte[32];
            for (int i = 0; i < length; i++) {
                keyBytes[i] = (byte) key.charAt(i);
            }
            
            int idx = 0;
            while (length < 32) { //32가 될 때까지 앞에 문자열 반복
                keyBytes[length] = keyBytes[idx];
                idx++;
                length++;
            }

            for (int i = 0; i < 4; i++) {
                this.key1 = this.key1 << 8;
                this.key1 = (int) (((long) this.key1) | EmoticonDecryptor.hash(keyBytes[i + 0]));
                this.key2 = this.key2 << 8;
                this.key2 = (int) (((long) this.key2) | EmoticonDecryptor.hash(keyBytes[i + 4]));
                this.key3 = this.key3 << 8;
                this.key3 = (int) (((long) this.key3) | EmoticonDecryptor.hash(keyBytes[i + 8]));
            }
            if (this.key1 == 0) {
                this.key1 = 301989938;
            }
            if (this.key2 == 0) {
                this.key2 = 623357073;
            }
            if (this.key3 == 0) {
                this.key3 = -2004086252;
            }
            return this;
        }

        public byte[] decryptBytes(byte[] data, int i) {
            byte[] decoded = new byte[data.length];
            for (int idx = 0; idx < data.length; idx++) {
                decoded[idx] = decryptByte(data[idx], i);
            }
            return decoded;
        }

        public final byte decryptByte(byte b2, int i) {
            byte b3 = 0;
            int i2 = 1;
            int i3 = 0;
            for (int i4 = 0; i4 < 8; i4++) {
                int i5 = this.key1;
                if ((i5 & 1) != 0) {
                    this.key1 = ((-2147483550 ^ i5) >> 1) | Integer.MIN_VALUE;
                    int i6 = this.key2;
                    if ((i6 & 1) != 0) {
                        this.key2 = ((i6 ^ 1073741856) >> 1) | -1073741824;
                        i2 = 1;
                    } else {
                        this.key2 = (i6 >> 1) & 1073741823;
                        i2 = 0;
                    }
                } else {
                    this.key1 = (i5 >> 1) & Integer.MAX_VALUE;
                    int i7 = this.key3;
                    if ((i7 & 1) != 0) {
                        this.key3 = ((i7 ^ 268435458) >> 1) | -268435456;
                        i3 = 1;
                    } else {
                        this.key3 = (i7 >> 1) & 268435455;
                        i3 = 0;
                    }
                }
                b3 = (byte) ((b3 << 1) | (i2 ^ i3));
            }
            byte b4 = (byte) (b2 ^ b3);
            return (i == 0 && b4 == 0) ? (byte) (b4 ^ b3) : b4;
        }
    }
}
