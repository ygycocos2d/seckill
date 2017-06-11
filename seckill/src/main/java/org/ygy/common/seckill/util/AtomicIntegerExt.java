package org.ygy.common.seckill.util;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicIntegerExt extends AtomicInteger {

	private static final long serialVersionUID = 7135811086491281608L;
	
	public AtomicIntegerExt(int value){
		super(value);
	}
	
	/**
	 * 当值大于0时，减1，返回原值
	 * @return
	 */
	public int getAndDecrementWhenGzero() {
		for (;;) {
            int current = get();
            int newValue = current;
            if (current > 0) {
            	newValue = current - 1;
            } 
            if (compareAndSet(current, newValue))
                return current;
        }
	}

	/**
	 * 增加指定数值，但最多不能超过指定最大值
	 * @param delta		增加的数值
	 * @param maxValue  最大值   
	 */
	public void addToMaxValueAtMost(int delta, int maxValue) {
		for (;;) {
            int current = get();
            int newValue = current + delta;
            if (newValue > maxValue) {
            	newValue = maxValue;
            }
            if (compareAndSet(current, newValue))
                break;
        }
	}

}
