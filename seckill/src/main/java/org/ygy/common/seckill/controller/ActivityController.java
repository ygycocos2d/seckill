package org.ygy.common.seckill.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ygy.common.seckill.dto.ActivityDTO;
import org.ygy.common.seckill.entity.ActivityEntity;
import org.ygy.common.seckill.entity.ActivityGoodsInventoryLogEntity;
import org.ygy.common.seckill.entity.GoodsEntity;
import org.ygy.common.seckill.entity.OrderEntity;
import org.ygy.common.seckill.entity.UserEntity;
import org.ygy.common.seckill.scheduler.ActivityInfo;
import org.ygy.common.seckill.scheduler.SchedulerContext;
import org.ygy.common.seckill.service.ActivityService;
import org.ygy.common.seckill.service.GoodsService;
import org.ygy.common.seckill.service.OrderService;
import org.ygy.common.seckill.util.AtomicIntegerExt;
import org.ygy.common.seckill.util.Constant;
import org.ygy.common.seckill.util.RedisUtil;
import org.ygy.common.seckill.util.StringUtil;

/**
 * 规定：
 * 1、状态为启动时，进行商品校验（该示例只进行库存校验和减库存操作）
 * 		所以增加、更新活动时，要判断状态。
 * 		如果增加时状态置为启动，则最后要减库存
 * 		如果修改时状态由停止转为启动，也要减库存
 * 2、状态由启动变为停止或删除，或者秒杀活动结束，进行还库存
 * 3、状态可以在启动停止间切换，也可以由启动、停止切到删除，但一旦删除便不能在对活动进行任何操作，包括状态修改
 * 4、总开关开启时，将所有状态为启动且为到开始时间的活动加入活动队列，并取出第一个加入调度队列开始调度
 * 5、总开关关闭时，清空活动队列；调度队列中，正在进行中的活动的调度任务不删除，其他删除
 * 
 * 注：1、对活动的所有操作的前提是---总开关是关闭状态
 * 	  2、总开关开启后，说明整个秒杀调度开始啦，最好不要再操作活动信息
 * @author ygy
 *
 */
@Controller
@RequestMapping("activity")
public class ActivityController {
	
	@Resource
	private ActivityService activityService;
	
	@Resource
	private GoodsService goodsService;
	
	@Resource
	private OrderService orderService;
	
	

	/**
	 * 秒杀
	 * @param request
	 * @return status -1-系统异常，0-秒杀成功，1-该用户已达秒杀数量上限
	 */
	@RequestMapping("kill")
	@ResponseBody
	public Map<String,Object> kill(HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", 0);
		try {
			UserEntity user = (UserEntity) request.getSession(true).getAttribute("user");
			String userId = user.getUserId();
			int num = SchedulerContext.getSucLog().get(userId);
			ActivityInfo curActivityInfo = SchedulerContext.getCurActivityInfo();
			// 是否达秒杀上限
			if (num < curActivityInfo.getNumLimit()) {
				AtomicIntegerExt goodsNum = curActivityInfo.getGoodsNum();
				// goods没被秒杀完，则秒杀成功，存记录
				if (goodsNum.getAndDecrementWhenGzero() > 0) {
					SchedulerContext.getSucLog().set(userId, (num+1));
					RedisUtil.setHashMapValue(Constant.GOODS_NUMBER+curActivityInfo.getActivityId(), 
							SchedulerContext.getAppno(), ""+goodsNum.get());
				} else {
					// 秒杀完了,秒杀结束
					result.put("status", 2);
				}
			} else {
				// 秒杀已达数量上限
				result.put("status", 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", -1);
		}
		return result;
	}
	
	/**
	 * 模拟支付（改订单状态）
	 * @param request
	 * @return status -1-系统异常，0-成功，1-该订单不存在
	 */
	@RequestMapping("pay")
	@ResponseBody
	public Map<String,Object> pay(HttpServletRequest request,String orderId) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", 0);
		try {
			String userId = "";
			UserEntity user = (UserEntity) request.getSession(true).getAttribute("user");
			if (null != user) {
				userId = user.getUserId();
			}
			OrderEntity order = this.orderService.getOrderByIdAndUserId(orderId, userId);
			if (null != order) {
				order.setStatus(Constant.ORDER_STATUS_PAYED);//模拟支付，只是改个状态
				this.orderService.update(order);
			} else {
				result.put("status", 1);
				result.put("msg", "该订单不存在！");
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", -1);
		}
		return result;
	}
	
	/**
	 * 批量修改秒杀活动状态
	 * @param activityIdList 以逗号分割的秒杀活动activityId列表
	 * @param status 
	 * @return
	 */
	@RequestMapping("updateStatus")
	@ResponseBody
	@Deprecated
	public Map<String,Object> updateStatus(String activityIdList, String status) {
		Map<String,Object> result = new HashMap<String,Object>();
		if (SchedulerContext.getMasterSwitch()) {
			result.put("status", 2);
			result.put("msg", "调度总开关已开启，当前操作不合法");
			return result;
		}
		if ( !StringUtil.contains(Constant.ACTIVITY_STATUS, status)) {
			result.put("status", 1);
			result.put("msg", "状态参数不合法");
			return result;
		} 
		try {
			String[] ids = activityIdList.split(",");
			List<String> idList = Arrays.asList(ids);
			Set<String> set = new HashSet<String>(idList);
			for (String id:set) {
				// 未开始且可修改状态（未删除的）的秒杀活动
				ActivityEntity activity = this.activityService.getNotDelActivityById(id);
				if (null != activity) {
					String oldStatus = activity.getStatus();
					if (null != oldStatus && !oldStatus.equals(status)) {
						activity.setStatus(status);
						//暂停-->启动，减库存
						if ("1".equals(oldStatus) && "0".equals(status)) {
							GoodsEntity goods = this.goodsService.getGoodsById(activity.getGoodsId());
							if (activity.getGoodsNumber() <= goods.getGoodsInventory()) {
								goods.setGoodsInventory(goods.getGoodsInventory()-activity.getGoodsNumber());
								ActivityGoodsInventoryLogEntity inventoryLog = new ActivityGoodsInventoryLogEntity();
								inventoryLog.setId(StringUtil.getUUID());
								inventoryLog.setActivityId(activity.getActivityId());
								inventoryLog.setGoodsId(goods.getGoodsId());
								inventoryLog.setGoodsInventory(-(activity.getGoodsNumber()));
								inventoryLog.setDescribt("商品-->活动");
								inventoryLog.setCreateTime(new Date());
								this.activityService.updateActivityAndGoods(activity,goods,inventoryLog);
							} else {
								result.put("status", 1);
								result.put("msg", "商品'"+goods.getGoodsName()+"'库存不足");
								return result;
							}
						}
						//启动-->暂停、删除，还库存
						else if ("0".equals(oldStatus) && !"0".equals(status)) {
							GoodsEntity goods = this.goodsService.getGoodsById(activity.getGoodsId());
							goods.setGoodsInventory(goods.getGoodsInventory()+activity.getGoodsNumber());
							ActivityGoodsInventoryLogEntity inventoryLog = new ActivityGoodsInventoryLogEntity();
							inventoryLog.setId(StringUtil.getUUID());
							inventoryLog.setActivityId(activity.getActivityId());
							inventoryLog.setGoodsId(goods.getGoodsId());
							inventoryLog.setGoodsInventory(activity.getGoodsNumber());
							inventoryLog.setDescribt("活动-->商品");
							this.activityService.updateActivityAndGoods(activity,goods,inventoryLog);
						} else {//暂停-->删除，只是改状态
							this.activityService.update(activity);
						}
					}
				}
			}
			result.put("status", 0);	
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", -1);	
			result.put("msg", "系统异常");
		}
		return result;
	}

	/**
	 * 为当前应用分配秒杀商品数（当为集群部署时）
	 * @param activityId
	 * @param number
	 * @return
	 */
	@RequestMapping("setGoodsNumForCurApp")
	@ResponseBody
	@Deprecated
	public Map<String,Object> setGoodsNumForCurApp(String activityId, Integer number) {
		Map<String,Object> result = new HashMap<String,Object>();
//		if (SchedulerContext.getMasterSwitch()) {
//			result.put("status", 2);
//			result.put("msg", "调度总开关已开启，当前操作不合法");
//			return result;
//		}
		if ( StringUtil.isEmpty(activityId) || null == number || number <= 0) {
			result.put("status", 1);
			result.put("msg", "参数不合法");
			return result;
		}
		try {
//			for(;;) {
//				ActivityEntity entity = this.activityService.getEntityById(activityId);
//				if (null != entity) {
//					int oldLeftGoodsNum = entity.getLeftGoodsNumber();
//					if (number <= oldLeftGoodsNum) {
//						int newLeftGoodsNum = oldLeftGoodsNum - number;
////						entity.setCurAppGoodsNum(number);
//						entity.setLeftGoodsNumber(newLeftGoodsNum);
//						boolean ok = this.activityService.updateWhenUnchanged(entity, oldLeftGoodsNum);
//						if (ok) {
//							// 存储该秒杀活动在当前应用中处理的商品量
//							SchedulerContext.setCurAppHandleGoodsNum(activityId, number);
//							result.put("status", 0);
//							break;
//						}
//					} else {
//						result.put("msg", "该应用设置的商品处理量大于秒杀活动总的商品量");
//						break;
//					}
//				} else {
//					result.put("msg", "该秒杀活动不存在");
//					break;
//				}
//			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", -1);
		}
		return result;
	}
	
	/**
	 * 启动调度总开关
	 * @return
	 */
	@RequestMapping("startMasterSwitch")
	@ResponseBody
	@Deprecated
	public Map<String,Object> startMasterSwitch() {
		Map<String,Object> result = new HashMap<String,Object>();
		if (SchedulerContext.getMasterSwitch()) {
			result.put("status", 1);
			result.put("msg", "调度总开关已开启");
			return result;
		}
		try {
//			// 状态为已启动且秒杀活动开始时间没过期
//			List<ActivityEntity> activityList = this.activityService.getAllEffectiveActivity();
//			if (null != activityList && !activityList.isEmpty()) {
//				// 将有效的秒杀活动放入优先级队列
//				SchedulerContext.getActivityQueue().addAll(activityList);
//				// 秒杀活动定时调度链启动
//				SchedulerContext.scheduleChainStart();
//				// 记录下总开关状态
//				SchedulerContext.setMasterSwitch(true);
//				result.put("status", 0);
//			} else {
//				result.put("status", 2);
//				result.put("msg", "没有任何有效的秒杀活动");
//			}
			SchedulerContext.setMasterSwitch(true);
		} catch (Exception e) {
			result.put("status", -1);
			result.put("msg", "系统异常");
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 关闭调度总开关（正在进行的活动无法关闭，已经在任务调度里了）
	 * @return
	 */
	@RequestMapping("stopMasterSwitch")
	@ResponseBody
	@Deprecated
	public Map<String,Object> stopMasterSwitch() {
		Map<String,Object> result = new HashMap<String,Object>();
		if (!SchedulerContext.getMasterSwitch()) {
			result.put("status", 1);
			result.put("msg", "调度总开关已关闭");
			return result;
		} 
		try {
			// 清空秒杀活动优先级队列
			SchedulerContext.getActivityQueue().removeAll();
			// 清空调度器中还没执行的活动的任务
			this.clearNoExecutedJobFromScheduler();
			// 记录下总开关状态
			SchedulerContext.setMasterSwitch(false);
			result.put("status", 0);
		} catch (Exception e) {
			result.put("status", -1);
			result.put("msg", "系统异常");
			e.printStackTrace();
		}
		return result;
	}
	
	@RequestMapping("addActivity")
	@ResponseBody
	@Deprecated
	public Map<String,Object> addActivity(@RequestBody ActivityDTO dto) {
		Map<String,Object> result = this.validateActivity(dto,true);
		try {
			//校通过验
			if (0 == (Integer)result.get("status")) {
				ActivityEntity activity = new ActivityEntity();
				this.dto2ActivityEntity(dto, activity);
				activity.setActivityId(StringUtil.getUUID());
				activity.setGroupId(StringUtil.getUUID());
				if (Constant.ACTIVITY_STATUS_START.equals(dto.getStatus())) {
					GoodsEntity goods = this.goodsService.getGoodsById(activity.getGoodsId());
					goods.setGoodsInventory(goods.getGoodsInventory()-activity.getGoodsNumber());
					ActivityGoodsInventoryLogEntity inventoryLog = new ActivityGoodsInventoryLogEntity();
					inventoryLog.setId(StringUtil.getUUID());
					inventoryLog.setActivityId(activity.getActivityId());
					inventoryLog.setGoodsId(goods.getGoodsId());
					inventoryLog.setGoodsInventory(-(activity.getGoodsNumber()));
					inventoryLog.setDescribt("商品-->活动");
					this.activityService.addActivityAndUpdateGoods(activity,goods,inventoryLog);
				} else {
					this.activityService.add(activity);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", -1);
			result.put("msg", "系统异常");
		}
		return result;
	}
	
	/**
	 * 
	 * @param entity
	 * @return
	 */
	@RequestMapping("updateActivity")
	@ResponseBody
	@Deprecated
	public Map<String,Object> updateActivity(@RequestBody ActivityDTO dto) {
		Map<String,Object> result = this.validateActivity(dto,false);
		try {
			//校通过验
			if (0 == (Integer)result.get("status")) {
				ActivityEntity en = this.activityService.getEntityById(dto.getActivityId());
				if (en != null) {
					if (en.getStartTime().compareTo(new Date()) > 0 && !"2".equals(en.getStatus())) {
						ActivityEntity activity = new ActivityEntity();
						this.dto2ActivityEntity(dto, activity);
						//暂停-->启动，减库存
						if ("1".equals(en.getStatus()) && "0".equals(activity.getStatus())) {
							GoodsEntity goods = this.goodsService.getGoodsById(activity.getGoodsId());
							goods.setGoodsInventory(goods.getGoodsInventory()-activity.getGoodsNumber());
							ActivityGoodsInventoryLogEntity inventoryLog = new ActivityGoodsInventoryLogEntity();
							inventoryLog.setId(StringUtil.getUUID());
							inventoryLog.setActivityId(activity.getActivityId());
							inventoryLog.setGoodsId(goods.getGoodsId());
							inventoryLog.setGoodsInventory(-(activity.getGoodsNumber()));
							inventoryLog.setDescribt("商品-->活动");
							this.activityService.updateActivityAndGoods(activity,goods,inventoryLog);
						} 
						//启动-->暂停、删除，还库存
						else if ("0".equals(en.getStatus()) && !"0".equals(activity.getStatus())) {
							GoodsEntity goods = this.goodsService.getGoodsById(activity.getGoodsId());
							goods.setGoodsInventory(goods.getGoodsInventory()+activity.getGoodsNumber());
							ActivityGoodsInventoryLogEntity inventoryLog = new ActivityGoodsInventoryLogEntity();
							inventoryLog.setId(StringUtil.getUUID());
							inventoryLog.setActivityId(activity.getActivityId());
							inventoryLog.setGoodsId(goods.getGoodsId());
							inventoryLog.setGoodsInventory(activity.getGoodsNumber());
							inventoryLog.setDescribt("活动-->商品");
							this.activityService.updateActivityAndGoods(activity,goods,inventoryLog);
						} else {//暂停-->删除，只是改状态
							this.activityService.update(activity);
						}
					} else {
						result.put("status", 1);
						result.put("msg", "该活动已过期或删除");
					}
				} else {
					result.put("status", 1);
					result.put("msg", "该活动不存在");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", -1);
			result.put("msg", "系统异常");
		}
		return result;
	}
	
	/**
	 * 获取所有状态为启动且未开始的秒杀活动
	 * @return
	 */
	@RequestMapping("getActivityListInStartup")
	@ResponseBody
	public Map<String,Object> getActivityListInStartup() {
		Map<String,Object> result = new HashMap<String,Object>();
		try {
			List<ActivityEntity> activityList = this.activityService.getAllEffectiveActivity();
			result.put("status", 0);
			result.put("data", activityList);
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", -1);
			result.put("msg", "系统异常");
		}
		return result;
	}
	
	/**
	 * 校验新增、修改活动时活动设置的正确性
	 * @param dto
	 * @param isAdd 
	 * @return
	 */
	private Map<String,Object> validateActivity(ActivityDTO dto, boolean isAdd) {
		Map<String,Object> result = new HashMap<String,Object>();
		// 校验通过
		result.put("status", 0);
		try {
			if (SchedulerContext.getMasterSwitch()) {
				result.put("status", 2);
				result.put("msg", "调度总开关已开启，当前操作不合法");
				return result;
			}
			if (null == dto) {
				result.put("status", 1);
				result.put("msg", "参数不能为空");
				return result;
			}
			if (!StringUtil.contains(Constant.ACTIVITY_STATUS, dto.getStatus())) {
				result.put("status", 1);
				result.put("msg", "状态参数不合法");
				return result;
			}
			if (isAdd && Constant.ACTIVITY_STATUS_DELETE.equals(dto.getStatus())) {
				result.put("status", 1);
				result.put("msg", "新增活动，状态不能为删除状态");
				return result;
			}
			// 开始时间要大于当前时间
			if (null == dto || null == dto.getStartTime() || 
					dto.getStartTime() <= System.currentTimeMillis() ) {
				result.put("status", 1);
				result.put("msg", "开始时间应该大于当前时间");
				return result;
			}
			// 结束时间一定要大于开始时间
			if (null == dto.getEndTime() || dto.getStartTime() >= dto.getEndTime()) {
				result.put("status", 1);
				result.put("msg", "结束时间应该大于开始时间");
				return result;
			}
			// 要指定商品
			if (StringUtil.isEmpty(dto.getGoodsId())) {
				result.put("status", 1);
				result.put("msg", "秒杀活动必须指定商品");
				return result;
			}
			// 秒杀商品数不能大于商品库存数
			GoodsEntity goods = this.goodsService.getGoodsById(dto.getGoodsId());
			if (null == goods) {
				result.put("status", 1);
				result.put("msg", "秒杀商品不存在");
				return result;
			} else if (goods.getGoodsInventory() < dto.getGoodsNumber()){
				result.put("status", 1);
				result.put("msg", "秒杀商品数不能大于商品库存数");
				return result;
			}
			// 开始时间、结束时间都不在已有的任意秒杀活动时间内(获取是数据库中活动时间包含新增的活动的开始时间或结束时间)
			ActivityEntity en = this.activityService.getByTime(
					dto.getActivityId(),new Date(dto.getStartTime()),
					new Date(dto.getEndTime()));
			if (null != en) { 
				result.put("status", 1);
				result.put("msg", "开始时间或结束时间不能在已有秒杀活动中");
				return result;
			}
		} catch (Exception e) {
			e.printStackTrace();
			result.put("status", -1);
			result.put("msg", "系统异常");
		}
		return result;
	}

	private void dto2ActivityEntity(ActivityDTO dto, ActivityEntity entity) {
		if (null != dto && null != entity) {
			entity.setActivityId(dto.getActivityId());
			entity.setGroupId(dto.getGroupId());
			entity.setGoodsId(dto.getGoodsId());
			entity.setStartTime(new Date(dto.getStartTime()));
			entity.setEndTime(new Date(dto.getEndTime()));
			entity.setGoodsNumber(dto.getGoodsNumber());
			entity.setGoodsPrice(dto.getGoodsPrice());
			entity.setLimitNumber(dto.getLimitNumber());
			entity.setDescribt(dto.getDescribt());
			entity.setPayDelay(dto.getPayDelay());
			entity.setStatus(dto.getStatus());
		}
	}

	private void clearNoExecutedJobFromScheduler() {
		ActivityInfo curActivityInfo = SchedulerContext.getCurActivityInfo();
		if (null != curActivityInfo && curActivityInfo.getStartTime() > System.currentTimeMillis()) {
			String name = curActivityInfo.getActivityId() + "_end";
			String group = curActivityInfo.getActivityGid() + "_end";
			SchedulerContext.getQuartzUtil().delete(name, group);
		}
	}
	
}
