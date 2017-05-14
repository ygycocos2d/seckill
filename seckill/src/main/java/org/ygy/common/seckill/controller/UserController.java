package org.ygy.common.seckill.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ygy.common.seckill.entity.UserEntity;

@Controller
@RequestMapping("user")
public class UserController {

	@RequestMapping("/login")
	@ResponseBody
	public Map<String,Object> login(@RequestBody UserEntity user) {
		Map<String,Object> result = new HashMap<String,Object>();
		return result;
	}
	
	@RequestMapping("logout")
	@ResponseBody
	public Map<String,Object> logout() {
		Map<String,Object> result = new HashMap<String,Object>();
		return result;
	}
	
	@RequestMapping("regist")
	@ResponseBody
	public Map<String,Object> regist(@RequestBody UserEntity user) {
		Map<String,Object> result = new HashMap<String,Object>();
		return result;
	}
}
