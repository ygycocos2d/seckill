package org.ygy.common.seckill.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.ygy.common.seckill.dao.SwitchDao;
import org.ygy.common.seckill.entity.SwitchEntity;
import org.ygy.common.seckill.entity.UserEntity;
import org.ygy.common.seckill.service.UserService;
import org.ygy.common.seckill.util.Constant;
import org.ygy.common.seckill.util.StringUtil;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Resource
	private UserService userService;
	
	@Resource
	private SwitchDao switchDao;

	/**
	 * 用户登录，增session
	 * @param user
	 * @return
	 */
	@RequestMapping("/login")
	@ResponseBody
	public Map<String,Object> login(@RequestBody UserEntity user,HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		if (null == user || StringUtil.isEmpty(user.getUserAccount()) 
				|| StringUtil.isEmpty(user.getUserPwd())) {
			result.put("status", 1);
			result.put("msg", "用户名密码必须有");
		} else {
			try {
				UserEntity userEntity = userService.getByAccount(user.getUserAccount());
				if (userEntity != null) {
					if (userEntity.getUserPwd().equals(user.getUserPwd())) {
						request.getSession().setAttribute("user", userEntity);
						result.put("status", 0);
					} else {
						result.put("status", 1);
						result.put("msg", "密码错误");
					}
				} else {
					result.put("status", 1);
					result.put("msg", "该用户不存在");
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("status", -1);
				result.put("msg", "系统异常");
			}
		}
		return result;
	}
	
	/**
	 * 用户退出，删session
	 * @return
	 */
	@RequestMapping("logout")
	@ResponseBody
	public Map<String,Object> logout(HttpServletRequest request) {
		Map<String,Object> result = new HashMap<String,Object>();
		request.getSession().invalidate();
		result.put("status", 0);
		return result;
	}
	
	/**
	 * 用户注册
	 * @param user
	 * @return
	 */
	@RequestMapping("regist")
	@ResponseBody
	public Map<String,Object> regist(@RequestBody UserEntity user) {
		Map<String,Object> result = new HashMap<String,Object>();
		if (null == user || StringUtil.isEmpty(user.getUserAccount()) 
				|| StringUtil.isEmpty(user.getUserPwd())) {
			result.put("status", 1);
			result.put("msg", "用户名密码必须有");
		} else {
			try {
				UserEntity en = userService.getByAccount(user.getUserAccount());
				if (en == null) {
					user.setUserId(StringUtil.getUUID());
					this.userService.add(user);
					result.put("status", 0);
				} else {
					result.put("status", 1);
					result.put("msg", "该账号已存在");
				}
			} catch (Exception e) {
				e.printStackTrace();
				result.put("status", -1);
				result.put("msg", "系统异常");
			}
		}
		return result;
	}
	
	@RequestMapping("testRegist")
	@ResponseBody
	public Map<String,Object> testRegist() {
		Map<String,Object> result = new HashMap<String,Object>();
		for(int i=0;i<100;i++) {
			UserEntity user = new UserEntity();
			user.setUserAccount("ygy"+i);
			user.setUserPwd("123456");
			regist(user);
		}
		return result;
	}
	
	@RequestMapping("getSwitch")
	@ResponseBody
	public Map<String,Object> getSwitch(String id) {
		Map<String,Object> result = new HashMap<String,Object>();
//		SwitchEntity en = switchDao.selectByPrimaryKey(id);
		SwitchEntity en = switchDao.selectByType(Constant.SECKILL_SWITCH);
		result.put("status", en.getStatus());
		return result;
	}
}
