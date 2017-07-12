package org.ygy.common.seckill.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.ygy.common.seckill.scheduler.KeepAliveJob;
import org.ygy.common.seckill.scheduler.MasterSwitchJob;
import org.ygy.common.seckill.scheduler.SchedulerContext;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

    private static JedisPool jedisPool;//连接池  
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
    
    public static void clearAll() {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
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
    
    public static String getHashMapValue(String key,String field) {
    	String value = null;
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		value = jedis.hget(key, field);
    		jedis.close();
    	} else {
    		
    	}
    	return value;
    }
    public static void setHashMapValue(String key,String field,String value) {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		jedis.hset(key, field, value);
    		jedis.close();
    	} else {
    		
    	}
    }
    
    public static void setEx(String key, String value, int seconds) {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		String ok = jedis.setex(key, seconds, value);
    		System.out.println("-----------------"+ok);
    		jedis.close();
    	} else {
    		
    	}
    }
    
    public static List<String> getByKeys(String... keys) {
    	List<String> values = new ArrayList<String>();
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		values = jedis.mget(keys);
    		jedis.close();
    	} else {
    		
    	}
    	return values;
    }
    public static void delete(String... keys) {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		jedis.del(keys);
    		jedis.close();
    	} else {
    		
    	}
    }
    public static void delete(String key) {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		jedis.del(key);
    		jedis.close();
    	} else {
    		
    	}
    }
    public static void hdelete(String key,String... fields) {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		jedis.hdel(key, fields);
    		jedis.close();
    	} else {
    		
    	}
    }
    public static Set<String> smembers(String key) {
    	Set<String> set = new HashSet<String>();
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		set = jedis.smembers(key);
    		jedis.close();
    	} else {
    		
    	}
    	return set;
    }
    public static void sadd(String key, String... members) {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		jedis.sadd(key, members);
    		jedis.close();
    	} else {
    		
    	}
    }
    
    
    
    public static String getAndSet(String key, String newValue) {
    	String oldValue = null;
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		oldValue = jedis.getSet(key, newValue);
    		jedis.close();
    	} else {
    		
    	}
    	return oldValue;
    }
    
    public static void incr(String key) {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		jedis.incr(key);
    		jedis.close();
    	} else {
    		
    	}
    }
    public static void decr(String key) {
    	if (null != jedisPool) {
    		Jedis jedis = jedisPool.getResource();
    		jedis.decr(key);
    		jedis.close();
    	} else {
    		
    	}
    }
    
    public static void main(String[] args) {
    	
    	getAndSet("","");
//    	String handled = RedisUtil.getHashMapValue(Constant.SUCC_HANDLED_FLAG, "55446");
//    	
//    	System.out.println(handled);
//    	Date date = new Date();
//    	SchedulerContext.getQuartzUtil().add(KeepAliveJob.class,
//    			"jsjdfhjsk", "jkafhja", date, 1*1000);
//    	
//    	SchedulerContext.getQuartzUtil().add(MasterSwitchJob.class, "a", "b", date);
    	
    	
//    	String key = Constant.GOODS_NUMBER+"jjakhfajk";
//    	Map<String, String> map = new HashMap<String, String>();
//    	map.put("1", Long.valueOf(45564654554L).toString());
//    	map.put("2", Long.valueOf(45564654554L).toString());
//    	map.put("3", Long.valueOf(45564654554L).toString());
//    	map.put("4", Long.valueOf(45564654554L).toString());
//    	map.put("5", Long.valueOf(45564654554L).toString());
//    	map.put("6", Long.valueOf(45564654554L).toString());
//    	setHashMap(key,map);
    	
//    	Map<String, String> map1 = getHashMap(key);
//    	List<String> keyList = new ArrayList<String>();
//    	for(Entry<String, String> en : map1.entrySet()) {
//    		keyList.add(Constant.KEEP_ALIVE + en.getKey());
//    	}
//    	String[] keys = keyList.toArray(new String[keyList.size()]);
//    	for (int i=0;i<keys.length;i++) {
//    		System.out.println(keys[i]+":");
//    	}
//    	
//    	setEx(Constant.KEEP_ALIVE+"0","0",3);
//    	setEx(Constant.KEEP_ALIVE+"1","1",3);
//    	setEx(Constant.KEEP_ALIVE+"2","2",3);
//    	setEx(Constant.KEEP_ALIVE+"3","3",3);
//    	setEx(Constant.KEEP_ALIVE+"4","4",3);
//    	setEx(Constant.KEEP_ALIVE+"5","5",3);
//    	setEx(Constant.KEEP_ALIVE+"0","0",30);
//    	setEx(Constant.KEEP_ALIVE+"1","1",30);
//    	setEx(Constant.KEEP_ALIVE+"2","2",30);
//    	setEx(Constant.KEEP_ALIVE+"3","3",30);
//    	setEx(Constant.KEEP_ALIVE+"4","4",30);
//    	setEx(Constant.KEEP_ALIVE+"5","5",30);
//    	List<String> appNoStatus = getByKeys(keys);
//    	System.out.println(appNoStatus);
//    	for (String status : appNoStatus) {
//    		if (null != status) {
//    			System.out.println(true);
//    		}
//    	}
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
    	
//    	String key = "keepalive";
//    	Map<String, String> map = new HashMap<String, String>();
//    	map.put("1", Long.valueOf(45564654554L).toString());//时间
//    	map.put("2", Long.valueOf(45564654554L).toString());
//    	map.put("3", Long.valueOf(45564654554L).toString());
//    	map.put("4", Long.valueOf(45564654554L).toString());
//    	map.put("5", Long.valueOf(45564654554L).toString());
//    	map.put("6", Long.valueOf(45564654554L).toString());
//    	
//    	setHashMap(key,map);
//    	
//    	System.out.println("------------------------");
//    	
//    	Map<String, String> map1 = getHashMap(key);
//    	
//    	System.out.println(map1);
    	
//    	System.out.println(getHashMapValue(key,"1"));;
    	
//    	String key2 = "activityId";
//    	Map<String, String> map2 = new HashMap<String, String>();
//    	map2.put("1", Long.valueOf(45564654554L).toString());//商品数量
//    	map2.put("2", Long.valueOf(45564654554L).toString());
//    	map2.put("3", Long.valueOf(45564654554L).toString());
//    	map2.put("4", Long.valueOf(45564654554L).toString());
//    	map2.put("5", Long.valueOf(45564654554L).toString());
//    	map2.put("6", Long.valueOf(45564654554L).toString());
    	
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
