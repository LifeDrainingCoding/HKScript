package com.lifedrained.libs;

public class AuthLinkRefactor {
   public static String changePlatform(String url){
       if(url.contains("Platform=weba")){
           url = url.replace("Platform=weba", "Platform=android");
       }
    return url;
     }
   }

