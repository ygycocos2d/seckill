package org.ygy.common.seckill.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ygy.common.seckill.scheduler.SchedulerContext;
import org.ygy.common.seckill.util.MyJob;

@Controller
@RequestMapping("test")
public class TestController {
	
	@RequestMapping("add")
	@ResponseBody
	public Map<String,Object> add() {
		Map<String,Object> result = new HashMap<String,Object>();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	try {
			Date date = dateFormat.parse("2017-5-16 9:30:10");
			SchedulerContext.add(MyJob.class, "j1", "g1", date);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		result.put("status", 0);
		return result;
	}
	
	@RequestMapping("start")
	@ResponseBody
	public Map<String,Object> start() {
		Map<String,Object> result = new HashMap<String,Object>();
		
		SchedulerContext.start();
		
		result.put("status", 0);
		return result;
	}
}