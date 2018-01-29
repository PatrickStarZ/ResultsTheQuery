package com.example.patrickstar.resultsthequery.Utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by PatrickStar on 2018/1/21.
 */

public class Md5Util {
    public static String ecodeTwice(String str) {
        return ecode(str);
    }

    public static String ecode(String passwd) {
        try {
            MessageDigest instance = MessageDigest.getInstance("MD5");
            byte[] digest = instance.digest(passwd.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                int i = b & 0xff;
                String hexString = Integer.toHexString(i);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                sb.append(hexString);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
