package org.ygy.common.seckill.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("friendly")
public class FriendlyController {
	
	@RequestMapping("timeLimit")
	@ResponseBody
	public Map<String,Object> timeLimit(HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", 5);
		result.put("msg", "当前没有进行的秒杀活动");
		return result;
	}
	
	@RequestMapping("accessLimit")
	@ResponseBody
	public Map<String,Object> accessLimit(HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		result.put("status", 6);
		result.put("msg", "人太多了");
		return result;
	}

}
