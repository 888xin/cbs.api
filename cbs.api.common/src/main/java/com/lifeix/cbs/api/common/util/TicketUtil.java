package com.lifeix.cbs.api.common.util;

import java.util.UUID;

public class TicketUtil {
    
    public static String getTicket() {
	return Md5Encrypt.md5(UUID.randomUUID().toString() + System.currentTimeMillis() + RandomUtils.createRandom(false, 8), "utf8");
    }
    
    public static String getOriginal(){
	return UUID.randomUUID().toString() + System.currentTimeMillis() + RandomUtils.createRandom(false, 8);
    }
    
    public static void main(String[] args) {
	System.out.println(getOriginal());
	System.out.println(Md5Encrypt.md5("9b477b59-e7b0-47ca-ae80-a23851ae524a1451011635488m317j6ma","utf-8"));
	/*for(int i = 0; i < 10000; i++) {
	    System.out.println(getTicket());
	}*/
    }
}
