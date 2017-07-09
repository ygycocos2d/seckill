package org.ygy.common.seckill.util;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.ygy.common.seckill.scheduler.SchedulerContext;

public class StringUtil {
	
	public static boolean isEmpty(String str) {
		if (null == str || "".equals(str.trim())) {
			return true;
		} 
		return false;
	}

	public static String getUUID() {
		return UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	public static String getClusterUUID() {
		return SchedulerContext.getAppno()+UUID.randomUUID().toString().replaceAll("-", "");
	}
	
	/**
     * 判断字串是否在字串数组中
     * @param stringArray
     * @param str
     * @return
     */
    public static boolean contains(String[] stringArray, String str) {
    	if (null == stringArray || stringArray.length <= 0 ) {
    		return false;
    	}
        List<String> tempList = Arrays.asList(stringArray);
        if(tempList.contains(str)) {
            return true;
        } else {
            return false;
        }
    }
	
	public static void main(String[] args) {
		System.out.println(getUUID());
		System.out.println(getClusterUUID());
//		String[] stringArray = {"a","b"};
//		System.out.println(contains(null,null));
		System.out.println("1".equals(null));
	}
}
