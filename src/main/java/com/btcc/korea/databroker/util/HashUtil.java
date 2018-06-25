package com.btcc.korea.databroker.util;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class HashUtil {

    public static String getSHA256(String str) {
        String sha = null;

       try {
            MessageDigest sh = MessageDigest.getInstance("SHA-256");

            sh.update(str.getBytes());

            byte byteData[] = sh.digest();

            StringBuffer sb = new StringBuffer();
            for(int i=0; i<byteData.length; i++) {
                sb.append(Integer.toString((byteData[i]&0xff) + 0x100, 16).substring(1));
            }
            sha = sb.toString();
        } catch(NoSuchAlgorithmException e) {
            log.error("", e);
        }

        return sha;
    }

}
