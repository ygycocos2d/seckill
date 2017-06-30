package org.ygy.common.seckill.scheduler;

import java.util.HashMap;
import java.util.Map;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 说明：应用实例是否dump机检测，频率为默认1秒，总开关开启后且为集群部署启动该定时任务
 * 策略:
 * 	1、活着；当前应用实例通过每秒在缓存（redis）中更新计数值，表示自己还活着，通过获取其他应用实例的redis中的该部分数据，可以判断其他实例是否活着。
 * 	2、当前实例over了，那没办法了。
 * 	3、其他实例over了（redis上的时间比当前时间少了3秒，我们认为相应应用实例dump），
 * 该部分数据在缓存中的格式：
 * 
 * 	1、"keepalive":Map<appno,计数值>
 *  作用：应用实例是否活着检测
 * 	说明：appno-应用实例号， 默认有一个虚拟appno为0，也就是说其他实例应用不能用0作为应用号，用于统一各个实际appno的计数值。
 *      计数值-每一个应用实例每秒从虚拟实例取出其对应计数值，进行加一操作，同时将操作后的计数值重置到自身实例及虚拟实例相应的计数值中。
 *      
 *  2、活动ID_goodsNumber:Map<appno,商品数>
 *  作用：记录应用实例当前秒杀活动还有多少秒杀商品，同时当应用实例宕机后，其他应用实例可以将该宕机实例的未抢完的商品数平分
 *  说明：没心情，没啥可说的
 *  
 *  3、活动ID_succLog:Map<userId,成功秒杀的商品数>
 * 	作用：记录用户某个秒杀活动中抢到的商品数
 * 	说明：言简意赅，也没啥可说
 * 
 * @author ygy
 *
 */
public class KeepAliveJob implements Job{

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		// TODO Auto-generated method stub
		
	}

}
