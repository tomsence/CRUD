package com.pm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

public interface BaseMapper {
	
	/*����*/
	public int insert(Map<String, Object> params);

	/*�޸�*/
	public int update(Map<String, Object> params);
	
	/*ɾ��*/
	public int delete(Map<String, Object> params);
	
	/*��һ��ѯ*/
	public HashMap query(Map<String, Object> params);
	
	/*������ѯ*/
	@MapKey("id")
	public HashMap queryAll(Map<String, Object> params);
	
	/*��ҳ��ѯ*/
	@MapKey("id")
	public HashMap queryPageAll(Map<String, Object> params);
	
	/*��ѯ����*/
	public long queryCount(Map<String, Object> params);
}
