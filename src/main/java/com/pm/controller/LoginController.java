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
	
	/*�û���¼*/
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
		System.out.println("controller�еõ���list���Ϊ:" + userList);
		if(userList.size() > 0) {	
			System.out.println("ȡ����infoΪ:" + userList.get(0));
			Map<String, Object> mapList = (Map<String, Object>) userList.get(0);
			
			if(mapList.get("user_passwd").equals(userPasswd)) {
				resultMap.put("user", mapList.get("user_name"));
				resultMap.put("status", Boolean.TRUE);
				resultMap.put("message", "��½�ɹ�");
				modelMap.addAttribute("mySession", mapList);
			}else {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("message", "�û��������벻��");
			}
		}else{
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("message", "�û��������ڣ���ע��ʹ��!");
		}
		
		JSONObject json = JSONObject.fromObject(resultMap);
		System.out.println(json);
		return resultMap;
	}
	
	/*�û��ǳ�*/
	@RequestMapping(value="/logout",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userLogout(HttpServletRequest request,UserInfo userInfo,String userName){
		Map<String, Object> resultMap = new HashMap<String,Object>();
		resultMap = (Map<String, Object>) request.getSession().getAttribute("mySession");
		System.out.println("session�е��û���Ϊ:" + resultMap.get("user_name"));
		System.out.println("ǰ���ϴ����û���Ϊ:" + userInfo.getUserName()+userName);
		if(resultMap.get("user_name").equals(userInfo.getUserName())) {
			resultMap.put("status", Boolean.TRUE);
			resultMap.put("message", "�ǳ��ɹ�");
			request.getSession().removeAttribute("mySession");
		}else{
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("message", "�û�����!");
		}
		return resultMap;
	}
	
	/*�û�ע��*/
	@RequestMapping(value="/userreg",method=RequestMethod.POST)
	/*
	 * @ResponseBodyע����������web���͵�pojo���󣻱�����json�������ݣ����ֱ�ӽ��ܶ���ʱ��
	 *��ʱҪ��json�е��ֶ��������pojo�������ơ��ֶ����ͱ�����ͬ������ᱨ400����
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
			resultMap.put("message", "�û����Ѵ��ڣ��뷵�ص�¼!");
		}else {
			ret = baseServiceClientImpl.insert(userInfo);
			if(ret == 1) {
				resultMap.put("status", Boolean.FALSE);
				resultMap.put("message", "ע��ɹ����뷵�ص�¼!");
			}
		}
		return resultMap;
	}
}