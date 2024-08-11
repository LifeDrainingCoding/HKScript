package com.lifedrained.libs;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.*;

public class MACGather {
    private static final Logger log = LogManager.getLogger(MACGather.class);

    public static String getMAC()  {
        NetworkInterface networkInterface = null;

        try {
            InetAddress localhost = InetAddress.getLocalHost();
           networkInterface = NetworkInterface.getByInetAddress(localhost);
        }catch (UnknownHostException ex){
            log.error(ex);
            log.warn("Unnable to find local host");
        }catch (SocketException ex){
            log.error(ex);
            log.warn("Unable to get net interface");
        }
        try{
            if(networkInterface != null){
                byte[] mac = networkInterface.getHardwareAddress();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));

                }
                log.info("MAC: {}", sb.toString());
                return sb.toString();
            }

        }catch (SocketException ex){
            log.error(ex);
            log.warn("Unable to get MAC bytes array");
        }
return null;
        }
}
