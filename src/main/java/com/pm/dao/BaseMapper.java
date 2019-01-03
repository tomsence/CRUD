package com.pm.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.MapKey;
import org.apache.ibatis.annotations.Param;

public interface BaseMapper {
	
	/*新增*/
	public int insert(Map<String, Object> params);

	/*修改*/
	public int update(Map<String, Object> params);
	
	/*删除*/
	public int delete(Map<String, Object> params);
	
	/*单一查询*/
	public HashMap query(Map<String, Object> params);
	
	/*条件查询*/
	@MapKey("id")
	public HashMap queryAll(Map<String, Object> params);
	
	/*分页查询*/
	@MapKey("id")
	public HashMap queryPageAll(Map<String, Object> params);
	
	/*查询总数*/
	public long queryCount(Map<String, Object> params);
}
