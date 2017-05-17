package org.ygy.common.seckill.controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ygy.common.seckill.entity.UserEntity;
import org.ygy.common.seckill.util.MyJob;
import org.ygy.common.seckill.util.QuartzUtil;

@Controller
@RequestMapping("test")
public class QuartzTestController {

	@RequestMapping("add")
	@ResponseBody
	public Map<String,Object> add() {
		Map<String,Object> result = new HashMap<String,Object>();
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date = null;
		try {
			date = dateFormat.parse("2017-5-15 22:48:10");
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		QuartzUtil.add(MyJob.class, "j1", "g1", date);
		QuartzUtil.start();
		return result;
	}
}
