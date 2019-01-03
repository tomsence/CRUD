package com.pm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public interface BaseServiceClient {
	
	public int insert(Object obj);
	
	public int delete(Object obj);
	
	public int update(Object obj);
	
	public HashMap select(long id, Class c);
	
	/*ȫ����ѯ*/
	public List selectAll(Object obj);
	
	/*��ҳ��ѯ*/
	public List selectPageAll(Object obj, Integer firstPage, Integer rows);
	
	/*��ѯ����*/
	public long queryCount(Object obj);
}
