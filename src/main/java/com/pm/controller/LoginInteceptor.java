package com.pm.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.pm.entity.UserInfo;

public class LoginInteceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request,HttpServletResponse response,Object handler) throws Exception{
		System.out.println("interceptor����!");
		String servletPath = request.getServletPath();
		HttpSession mySession = request.getSession();
		System.out.println("===="+servletPath+"=====");
		
		if(servletPath.equals("/login")||servletPath.equals("/index.html")) {
			System.out.println("��¼����������,ʲô��������!");
			return true;
		}else{
			System.out.println("����¼�����·��");
			Map<String, Object> userMap = (Map<String, Object>) mySession.getAttribute("mySession");
			System.out.println(userMap.get("user_name"));
			return true;
		}	
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
