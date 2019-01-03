package com.pm.service;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pm.annotation.Column;
import com.pm.annotation.Id;
import com.pm.annotation.Table;
import com.pm.dao.BaseMapper;

@Service
public class BaseServiceClientImpl implements BaseServiceClient {
	@Autowired
	private  BaseMapper baseMapper;
	
	private Map<String, Object> tranFromObj(Object obj, String type){
		Map<String, Object> resultMap = new HashMap<String,Object>();
		
		String tabName = obj.getClass().getAnnotation(Table.class).value();	//获取表名
		resultMap.put("TABLE_NAME", tabName);
		
		Method[] methods = obj.getClass().getMethods();	//获取方法
	
		if("insert".equals(type)) {
			/*增加，k为列表 v为列表*/
			List k = new ArrayList();	//存字段名
			List v = new ArrayList();	//存字段值
			
			for (Method m : methods) {
				if( null != m.getAnnotation(Column.class) ) {
					k.add(m.getAnnotation(Column.class).value());
					try {
						v.add(m.invoke(obj, null));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			if( k.size() != v.size() ) {
				System.out.println("长度不一致!");
			}
			resultMap.put("COLUMNS",k);
			resultMap.put("VALUES", v);
		}else if("update".equals(type)) {
			/*修改，k=v,内部是一个map，中间是一个列表，最外部还是一个map*/
			List d = new ArrayList();
			for (Method m : methods) {
				if( null != m.getAnnotation(Column.class)) {
					Map map = new HashMap();
					map.put("COLUMN", m.getAnnotation(Column.class).value());
					try {
						map.put("COL_VALUE",m.invoke(obj, null));
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
					d.add(map);
				}
				
				if( null != m.getAnnotation(Id.class) ) {
					resultMap.put("KEY_ID", m.getAnnotation(Id.class).value());
					try {
						resultMap.put("KEY_VALUE", m.invoke(obj, null) );
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
			resultMap.put("DATA", d);
		}else if("common".equals(type)) {
			/*通过ID进行删除、查询*/
			for(Method m : methods) {
				if( null != m.getAnnotation(Id.class) ) {
					resultMap.put("KEY_ID", m.getAnnotation(Id.class).value());
					try {
						resultMap.put("KEY_VALUE", m.invoke(obj, null) );
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
						e.printStackTrace();
					}
				}
			}
		}else if("selectAll".equals(type)) {
			List d = new ArrayList();
			for (Method m : methods) {
				if( null != m.getAnnotation(Column.class)) {
					System.out.println("取到的列名有："+ m.getAnnotation(Column.class).value());
					System.out.println("取到的键值有："+ m.getAnnotation(Column.class).key());
					Map map = new HashMap();
					try {
						if( "Y".equals(m.getAnnotation(Column.class).key() )  &&  null != m.invoke(obj, null)) {
							map.put("COLUMN", m.getAnnotation(Column.class).value());
							map.put("COL_VALUE", m.invoke(obj, null));
							d.add(map);
						}
					} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e1) {
						e1.printStackTrace();
					}
				}
			}
			if(d.size() == 0) {
				Map map = new HashMap();
				map.put("COLUMN","1");
				map.put("COL_VALUE","1");
				d.add(map);
			}
			System.out.println("最终得到的列表为:" + d);
			resultMap.put("DATA", d);
		}	
		return resultMap;
	}
	
	private List tranFromMap(Map mapList) {
		List<Object> list = new ArrayList<Object>();
		if(mapList.size() >0 ) {
			Iterator<Integer> it = mapList.keySet().iterator();
			while( it.hasNext() ) {
				List<Object> list_tmp = new ArrayList<Object>();
				Integer key = it.next();
				Map mapChild = (Map) mapList.get(key);
				list.add(mapChild);
			}
		}
		System.out.println("最终生成的list为:" + list);
		return list;
	}
	
	@Override
	public int insert(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>();
		params = tranFromObj(obj, "insert");
		return baseMapper.insert(params);
	}

	@Override
	public int delete(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>();
		params = tranFromObj(obj, "common");
		return baseMapper.delete(params);
	}

	@Override
	public int update(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>();
		params = tranFromObj(obj, "update");
		return baseMapper.update(params);
		
	}

	@Override
	public HashMap select(long id, Class c) {
		Map<String, Object> params = new HashMap<String, Object>();
		try {
			params = tranFromObj(c.newInstance(), "common");
		} catch (InstantiationException | IllegalAccessException e) {
			e.printStackTrace();
		}
		params.put("KEY_VALUE", id);
		return baseMapper.query(params);
	}

	@Override
	public List selectAll(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> queryall = new HashMap<String, Object>();
		params = tranFromObj(obj, "selectAll");
		queryall = baseMapper.queryAll(params);
		System.out.println("生成的map为:" + queryall );
		return tranFromMap(queryall);
	}
	
	@Override
	public List selectPageAll(Object obj, Integer firstPage, Integer rows) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> queryall = new HashMap<String, Object>();
		params = tranFromObj(obj, "selectAll");
		params.put("firstPage", firstPage);
		params.put("rows", rows);
		queryall = baseMapper.queryPageAll(params);
		System.out.println("生成的map为:" + queryall );
		return tranFromMap(queryall);
	}
	
	@Override
	public long queryCount(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>();
		String tabName = obj.getClass().getAnnotation(Table.class).value();	//获取表名
		params.put("TABLE_NAME", tabName);
		return baseMapper.queryCount(params);
	}	
}
