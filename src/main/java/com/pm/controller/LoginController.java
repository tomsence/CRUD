package com.pm.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.pm.entity.UserInfo;
import com.pm.service.BaseServiceClientImpl;

import net.sf.json.JSONObject;

@Controller
@SessionAttributes("mySession")
public class LoginController {
	@Autowired
	private BaseServiceClientImpl baseServiceClientImpl;
	
	/*用户登录*/
	@RequestMapping(value="/login",method = RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userLogin(@RequestParam("username")String userName,
									@RequestParam("userpasswd")String userPasswd,ModelMap modelMap) {
		System.out.println("========"+ userName);
		
		Map<String, Object> resultMap = new HashMap<String,Object>();
		
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(userName);
		userInfo.setUserPasswd(userPasswd);
		
		List<Object> userList = baseServiceClientImpl.selectAll(userInfo);
		System.out.println("controller中得到的list结果为:" + userList);
		if(userList.size() > 0) {	
			System.out.println("取到的info为:" + userList.get(0));
			Map<String, Object> mapList = (Map<String, Object>) userList.get(0);
			
			if(mapList.get("user_passwd").equals(userPasswd)) {
				resultMap.put("user", mapList.get("user_name"));
				resultMap.put("status", Boolean.TRUE);
				resultMap.put("message", "登陆成功");
				modelMap.addAttribute("mySession", mapList);
			}else {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("message", "用户名与密码不符");
			}
		}else{
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("message", "用户名不存在，请注册使用!");
		}
		
		JSONObject json = JSONObject.fromObject(resultMap);
		System.out.println(json);
		return resultMap;
	}
	
	/*用户登出*/
	@RequestMapping(value="/logout",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userLogout(HttpServletRequest request,UserInfo userInfo,String userName){
		Map<String, Object> resultMap = new HashMap<String,Object>();
		resultMap = (Map<String, Object>) request.getSession().getAttribute("mySession");
		System.out.println("session中的用户名为:" + resultMap.get("user_name"));
		System.out.println("前端上传的用户名为:" + userInfo.getUserName()+userName);
		if(resultMap.get("user_name").equals(userInfo.getUserName())) {
			resultMap.put("status", Boolean.TRUE);
			resultMap.put("message", "登出成功");
			request.getSession().removeAttribute("mySession");
		}else{
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("message", "用户不符!");
		}
		return resultMap;
	}
	
	/*用户注册*/
	@RequestMapping(value="/userreg",method=RequestMethod.POST)
	/*
	 * @ResponseBody注解用来接受web传送的pojo对象；比如用json传递数据，后端直接接受对象时，
	 *这时要求json中的字段名必须和pojo对象名称、字段类型必须相同，否则会报400错误。
	**/
	@ResponseBody
	//public Map<String, Object> userRegesit(UserInfo userInfo) {
	public Map<String, Object> userRegesit(@RequestBody UserInfo userInfo) {
		Map<String, Object> resultMap = new HashMap<String,Object>();
		int ret = -1;
		System.out.println("uuuuuuuuuuuuuuuuuuuu");
		//List<UserInfo> userList = userService.getUserPassByUser(userInfo.getUserName(), userInfo.getUserPasswd());

		List<UserInfo> userList = baseServiceClientImpl.selectAll(userInfo);
		
		if(userList.size()>0) {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("message", "用户名已存在，请返回登录!");
		}else {
			ret = baseServiceClientImpl.insert(userInfo);
			if(ret == 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("message", "注册成功，请返回登录!");
			}
		}
		return resultMap;
	}
}