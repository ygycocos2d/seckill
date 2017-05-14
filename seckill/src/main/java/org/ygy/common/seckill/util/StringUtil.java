package org.ygy.common.seckill.util;

import java.util.UUID;

public class StringUtil {

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static void main(String[] args) {
		System.out.println(getUUID());
	}
}
