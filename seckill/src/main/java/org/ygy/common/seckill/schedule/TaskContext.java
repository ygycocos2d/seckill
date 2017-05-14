package org.ygy.common.seckill.schedule;

import java.util.PriorityQueue;
import java.util.Queue;

import org.ygy.common.seckill.entity.TaskEntity;

public class TaskContext {

	private Queue<TaskEntity> taskQueue = new PriorityQueue<TaskEntity>();
}
