package org.ygy.common.seckill.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.ygy.common.seckill.scheduler.SchedulerContext;

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
    private static String auth;
    
    static {
    	InputStream in = null;
    	try {
    		in = RedisUtil.class.getClassLoader().getResourceAsStream("conf/redis.properties");
    		Properties prop = new Properties();
    		prop.load(in);
//    		startup = prop.getProperty("startup");
//    		if (null != startup && "on".equals(startup.trim())) {
    		if (SchedulerContext.isCluster()) {//集群才会用到redis
    			maxIdle = prop.getProperty("maxIdle");
    			maxActive = prop.getProperty("maxActive");
    			maxWait = prop.getProperty("maxWait");
    			testOnBorrow = prop.getProperty("testOnBorrow");
    			host = prop.getProperty("host");
    			port = prop.getProperty("port");
    			auth = prop.getProperty("auth");
    			JedisPoolConfig config = new JedisPoolConfig();   
                config.setMaxIdle(Integer.parseInt(maxIdle));   
                config.setMaxTotal(Integer.parseInt(maxActive));   
                config.setMaxWaitMillis(Long.parseLong(maxWait));   
                config.setTestOnBorrow("false".endsWith(testOnBorrow)?false:true);  
//                jedisPool = new JedisPool(config, host, Integer.parseInt(port));  
                jedisPool = new JedisPool(config, host, Integer.parseInt(port), 0, auth);  
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
    
    public static void setHashMap(String key, Map<String, String> map) {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		jedis.hmset(key, map);
    		jedis.close();
    	} else {
    		
    	}
    }
    public static Map<String, String> getHashMap(String key) {
    	Map<String, String> map = new HashMap<String,String>();
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		map = jedis.hgetAll(key);
    		jedis.close();
    	} else {
    		
    	}
    	return map;
    }
    
    public static void main(String[] args) {
    	
    	String key = "keepalive";
    	Map<String, String> map = new HashMap<String, String>();
    	map.put("1", Long.valueOf(45564654554L).toString());//时间
    	map.put("2", Long.valueOf(45564654554L).toString());
    	map.put("3", Long.valueOf(45564654554L).toString());
    	map.put("4", Long.valueOf(45564654554L).toString());
    	map.put("5", Long.valueOf(45564654554L).toString());
    	map.put("6", Long.valueOf(45564654554L).toString());
    	
    	setHashMap(key,map);
    	
    	System.out.println("------------------------");
    	
    	Map<String, String> map1 = getHashMap(key);
    	
    	System.out.println(map1);
    	
    	
    	String key2 = "activityId";
    	Map<String, String> map2 = new HashMap<String, String>();
    	map2.put("1", Long.valueOf(45564654554L).toString());//商品数量
    	map2.put("2", Long.valueOf(45564654554L).toString());
    	map2.put("3", Long.valueOf(45564654554L).toString());
    	map2.put("4", Long.valueOf(45564654554L).toString());
    	map2.put("5", Long.valueOf(45564654554L).toString());
    	map2.put("6", Long.valueOf(45564654554L).toString());
    	
//    	clearAll();
//    	long start = System.currentTimeMillis();
//    	for (int i=0;i<10000;i++) {
//    		set("key-11","val-11");
//    	}
//    	System.out.println(System.currentTimeMillis()-start);
//    	
//    	start = System.currentTimeMillis();
//    	System.out.println(getAll().size());
//    	System.out.println(System.currentTimeMillis()-start);
    	
    }
    	
}
