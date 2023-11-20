package com.lld.im.codec.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class CommonUtils {

    public static String getIpAddr(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

}
