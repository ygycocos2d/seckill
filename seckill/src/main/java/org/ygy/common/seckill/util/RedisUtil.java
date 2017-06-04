package org.ygy.common.seckill.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

    private static JedisPool jedisPool;//连接池  
    private static String startup;
    private static String host;
    private static String port;
    private static String maxIdle;
    private static String maxActive;
    private static String maxWait;
    private static String testOnBorrow;
    
    static {
    	InputStream in = null;
    	try {
    		in = RedisUtil.class.getClassLoader().getResourceAsStream("conf/redis.properties");
    		Properties prop = new Properties();
    		prop.load(in);
    		startup = prop.getProperty("startup");
    		if (null != startup && "on".equals(startup.trim())) {
    			maxIdle = prop.getProperty("maxIdle");
    			maxActive = prop.getProperty("maxActive");
    			maxWait = prop.getProperty("maxWait");
    			testOnBorrow = prop.getProperty("testOnBorrow");
    			host = prop.getProperty("host");
    			port = prop.getProperty("port");
    			JedisPoolConfig config = new JedisPoolConfig();   
                config.setMaxIdle(Integer.parseInt(maxIdle));   
                config.setMaxTotal(Integer.parseInt(maxActive));   
                config.setMaxWaitMillis(Long.parseLong(maxWait));   
                config.setTestOnBorrow("false".endsWith(testOnBorrow)?false:true);  
                jedisPool = new JedisPool(config, host, Integer.parseInt(port));  
    		}
        } catch (Exception e) {
        	e.printStackTrace();
        } finally {
        	if (null != in) {
        		try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
        	}
        }
    }
    
    public static void set(String key, String value) {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		jedis.set(key, value);
    		jedis.close();
    	} else {
    		
    	}
    }
    
    public static String get(String key) {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		String value = jedis.get(key);
    		jedis.close();
    		return value;
    	} else {
    		
    	}
    	return null;
    }
    
    public static Map<String, String> getAll() {
    	Map<String, String> map = new HashMap<String, String>();
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		Set<String> set = jedis.keys("*");
    		Iterator<String> iterator = set.iterator();
    		while (iterator.hasNext()) {
    			String key = iterator.next();
    			map.put(key, jedis.get(key));
    		}
    		jedis.close();
    	} else {
    		
    	}
    	return map;
    }
//    
//    public static int getNum() {
//    	int total = 0;
//    	if (null != jedisPool) {
//    		Jedis jedis = jedisPool.getResource();
//    		jedis.g
//    	} else {
//    		
//    	}
//    	return total;
//    }
    
    public static void clearAll() {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
//    		jedis.del("*");
    		jedis.flushAll();
    		jedis.close();
    	} else {
    		
    	}
    }
    
    public static void main(String[] args) {
    	clearAll();
    	long start = System.currentTimeMillis();
    	for (int i=0;i<10000;i++) {
    		set("key"+i,"val"+i);
    	}
    	System.out.println(System.currentTimeMillis()-start);
    	
    	start = System.currentTimeMillis();
    	System.out.println(getAll().size());
    	System.out.println(System.currentTimeMillis()-start);
    	
    }
    	
}
