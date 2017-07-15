package org.ygy.common.seckill.util;

import java.util.List;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

/**
 * redis分布式锁
 * @author gy
 *
 */
public class RedisLock {
	
	/**
	 * true-互斥锁；false-一次锁，只能获取一次，获取后永不释放。
	 */
	protected boolean mutex;
	/**
	 * 高并发，快速响应。true-快速响应获取锁，false-只要并发有序获取锁就行，不着急。
	 */
	protected boolean fast;
	/**
	 * 锁键
	 */
	protected String lockKey;
	/**
	 * 锁值
	 */
	protected String lockValue;
	
	/**
	 * 默认互斥锁，非高并发快速响应场景
	 * @param lockKey	锁键
	 */
	public RedisLock(String lockKey) {
		this(lockKey,true,false);
	}
	/**
	 * 默认非高并发快速响应场景
	 * @param lockKey	锁键
	 * @param mutex		是否互斥锁
	 */
	public RedisLock(String lockKey, boolean mutex) {
		this(lockKey,mutex,false);
	}
	/**
	 * 分布式锁
	 * @param lockKey	锁键
	 * @param mutex		是否互斥锁
	 * @param fast		是否不断请求锁，不等
	 */
	public RedisLock(String lockKey, boolean mutex, boolean fast) {
		this.lockKey = lockKey;
		this.mutex = mutex;
		this.fast = fast;
		lockValue = StringUtil.getClusterUUID();
	}

	/**
	 * 获取锁
	 * @param conn				redis连接
	 * @param acquireTimeout	获取锁，请求超时，单位毫秒
	 * @param lockTimeout		锁过期设置，单位毫米
	 * @return
	 */
	public boolean acquireLockWithTimeout(Jedis conn, long acquireTimeout, long lockTimeout) {
		if (null == conn) {
			return false;
		}
		if (!this.mutex) {
			 return 1==conn.setnx(this.lockKey, this.lockValue)?true:false;
		}
		int lockExpire = (int)(lockTimeout / 1000);//锁的过期时间
        long end = System.currentTimeMillis() + acquireTimeout;//尝试获取锁的时限
        while (System.currentTimeMillis() < end) {//判断是否超过获取锁的时限
            if (conn.setnx(this.lockKey, this.lockValue) == 1){//判断设置锁的值是否成功
            	conn.expire(this.lockKey, lockExpire);//设置锁的过期时间
                return true;          
            }
            if (conn.ttl(this.lockKey) == -1) {//判断锁是否设置超时
                conn.expire(this.lockKey, lockExpire);
            }
            if (!this.fast) {//只保证并发有序，不保证快速响应
            	try {
                    Thread.sleep(500);//等待500毫秒后重新尝试设置锁的值
                }catch(InterruptedException ie){
                    Thread.currentThread().interrupt();
                }
            }
        }
        return false;
    }
	
	/**
	 * 释放锁。若为一次锁可以不调用该方法，即使调用了也不会释放。
	 * @param conn
	 * @return
	 */
	public boolean releaseLock(Jedis conn) {
		if (null == conn) {
			return false;
		}
		if (!this.mutex) {
			return true;
		}
        while (true){
            conn.watch(this.lockKey);    //监视锁的键
            if (this.lockValue.equals(conn.get(this.lockKey))){  //判断锁的值是否和加锁时设置的一致，即检查进程是否仍然持有锁
                Transaction trans = conn.multi();
                trans.del(this.lockKey);             //在Redis事务中释放锁
                List<Object> results = trans.exec();
                if (results == null){   
                    continue;       //事务执行失败后重试（监视的键被修改导致事务失败，重新监视并释放锁）
                }
                return true;
            }
            conn.unwatch();     //解除监视
            break;
        }
        return false;
    }
}
