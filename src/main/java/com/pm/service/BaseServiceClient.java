package com.pm.service;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

public interface BaseServiceClient {
	
	public int insert(Object obj);
	
	public int delete(Object obj);
	
	public int update(Object obj);
	
	public HashMap select(long id, Class c);
	
	/*全量查询*/
	public List selectAll(Object obj);
	
	/*分页查询*/
	public List selectPageAll(Object obj, Integer firstPage, Integer rows);
	
	/*查询总数*/
	public long queryCount(Object obj);
}
