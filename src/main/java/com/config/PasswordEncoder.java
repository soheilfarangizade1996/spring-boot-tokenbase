package com.config;

import org.springframework.stereotype.Component;
import java.security.MessageDigest;

@Component
public class PasswordEncoder {


    public String encodePassSHA1(String input) {
        StringBuilder sb = new StringBuilder();
        try {
            MessageDigest mDigest = MessageDigest.getInstance("SHA1");
            byte[] result = mDigest.digest(input.getBytes());
            for (int i = 0; i < result.length; i++) {
                sb.append(Integer.toString((result[i] & 0xff) + 0x100, 16).substring(1));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

}
