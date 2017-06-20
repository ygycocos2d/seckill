package org.ygy.common.seckill.util;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

import org.ygy.common.seckill.entity.ActivityEntity;

public class ActivityQueue {

	private Queue<ActivityEntity> activityQueue = new PriorityQueue<ActivityEntity>(16, 
			new Comparator<ActivityEntity>() {
				@Override
				public int compare(ActivityEntity o1, ActivityEntity o2) {
					return o1.getStartTime().compareTo(o2.getStartTime());
				}
	});
	
	public ActivityEntity getHeaderNotDel() {
		return activityQueue.peek();
	}
	
	public boolean isEmpty() {
		return activityQueue.isEmpty();
	}
	
	public ActivityEntity getHeaderAndDel() {
		return activityQueue.poll();
	}
	
	public boolean exist(String activityId) {
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
	 
	 public void add(ActivityEntity entity) {
		 if (null != entity) {
			 activityQueue.add(entity);
		 }
	 }
	 
	 public void addAll(List<ActivityEntity> entitys) {
		 if (null != entitys && !entitys.isEmpty()) {
			 activityQueue.addAll(entitys);
		 }
	 }
	 
	 public void removeAll() {
		 activityQueue.clear();
	 }
	 
	 public void delete(String activityId) {
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

//		 ActivityEntity e1 = new ActivityEntity();
		 ActivityQueue a = new ActivityQueue();
		 System.out.println(a.isEmpty());
//		 e1.setStartTime(new Date(System.currentTimeMillis() + 10000));
//		 activityQueue.add(e1);
//		 ActivityEntity e2 = new ActivityEntity();
//		 e2.setStartTime(new Date(System.currentTimeMillis()));
//		 activityQueue.add(e2);
//		 ActivityEntity e3 = new ActivityEntity();
//		 e3.setStartTime(new Date(System.currentTimeMillis()-12540));
//		 activityQueue.add(e3);
//		 
//		 System.out.println(activityQueue.poll().getStartTime());
//		 System.out.println(activityQueue.poll().getStartTime());
//		 System.out.println(activityQueue.poll().getStartTime());


	 }
}
