package org.ygy.common.seckill.scheduler;

import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.util.StringUtil;

public class ActivityQueue {

	private static Queue<ActivityEntity> activityQueue = new PriorityQueue<ActivityEntity>(16, 
			new Comparator<ActivityEntity>() {
				@Override
				public int compare(ActivityEntity o1, ActivityEntity o2) {
					return o1.getStartTime().compareTo(o2.getStartTime());
				}
	});
	
	public static ActivityEntity getHeader() {
		return activityQueue.poll();
	}
	
	public static boolean exist(String activityId) {
		 if (StringUtil.isEmpty(activityId)) {
			 return false;
		 }
		 boolean exist = false;
		 for (ActivityEntity entity : activityQueue) {
			 if (activityId.equals(entity.getActivityId())) {
				 exist = true;
				 break;
			 }
		 }
		 return exist;
	 }
	 
	 public static void add(ActivityEntity entity) {
		 if (null != entity) {
			 activityQueue.add(entity);
		 }
	 }
	 
	 public static void addAll(List<ActivityEntity> entitys) {
		 if (null != entitys && !entitys.isEmpty()) {
			 activityQueue.addAll(entitys);
		 }
	 }
	 
	 public static void removeAll() {
		 activityQueue.clear();
	 }
	 
	 public static void delete(String activityId) {
		 if (!StringUtil.isEmpty(activityId)) {
			 Iterator<ActivityEntity> ite = activityQueue.iterator();
			 while (ite.hasNext()) {
				 ActivityEntity entity = ite.next();
				 if (activityId.equals(entity.getActivityId())) {
					 ite.remove();
				 }
			 }
		 }
	 }
	 
	 public static void main(String[] args) {

		 ActivityEntity e1 = new ActivityEntity();
		 e1.setStartTime(new Date(System.currentTimeMillis() + 10000));
		 activityQueue.add(e1);
		 ActivityEntity e2 = new ActivityEntity();
		 e2.setStartTime(new Date(System.currentTimeMillis()));
		 activityQueue.add(e2);
		 ActivityEntity e3 = new ActivityEntity();
		 e3.setStartTime(new Date(System.currentTimeMillis()-12540));
		 activityQueue.add(e3);
		 
		 System.out.println(activityQueue.poll().getStartTime());
		 System.out.println(activityQueue.poll().getStartTime());
		 System.out.println(activityQueue.poll().getStartTime());


	 }
}
