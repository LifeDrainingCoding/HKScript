package com.lifedrained.libs;



import java.net.*;
import static com.lifedrained.libs.Singletone.log;
public class MACGather {


    public static String getMAC()  {
        NetworkInterface networkInterface = null;

        try {
            InetAddress localhost = InetAddress.getLocalHost();
           networkInterface = NetworkInterface.getByInetAddress(localhost);
        }catch (UnknownHostException ex){
            log.throwing(MACGather.class.getName(),"getMac", ex);
            log.warning("Unnable to find local host");
        }catch (SocketException ex){
            log.throwing(MACGather.class.getName(),"getMac", ex);
            log.warning("Unable to get net interface");
        }
        try{
            if(networkInterface != null){
                byte[] mac = networkInterface.getHardwareAddress();
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < mac.length; i++) {
                    sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));

                }

                return sb.toString();
            }

        }catch (SocketException ex){
            log.throwing(MACGather.class.getName(),"getMac",ex);
            log.warning("Unable to get MAC bytes array");
        }
        return null;
        }
}
