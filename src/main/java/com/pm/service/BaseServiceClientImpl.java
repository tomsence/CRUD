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
		
		String tabName = obj.getClass().getAnnotation(Table.class).value();	//��ȡ����
		resultMap.put("TABLE_NAME", tabName);
		
		Method[] methods = obj.getClass().getMethods();	//��ȡ����
	
		if("insert".equals(type)) {
			/*���ӣ�kΪ�б� vΪ�б�*/
			List k = new ArrayList();	//���ֶ���
			List v = new ArrayList();	//���ֶ�ֵ
			
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
				System.out.println("���Ȳ�һ��!");
			}
			resultMap.put("COLUMNS",k);
			resultMap.put("VALUES", v);
		}else if("update".equals(type)) {
			/*�޸ģ�k=v,�ڲ���һ��map���м���һ���б����ⲿ����һ��map*/
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
			/*ͨ��ID����ɾ������ѯ*/
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
					System.out.println("ȡ���������У�"+ m.getAnnotation(Column.class).value());
					System.out.println("ȡ���ļ�ֵ�У�"+ m.getAnnotation(Column.class).key());
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
			System.out.println("���յõ����б�Ϊ:" + d);
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
		System.out.println("�������ɵ�listΪ:" + list);
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
		System.out.println("���ɵ�mapΪ:" + queryall );
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
		System.out.println("���ɵ�mapΪ:" + queryall );
		return tranFromMap(queryall);
	}
	
	@Override
	public long queryCount(Object obj) {
		Map<String, Object> params = new HashMap<String, Object>();
		String tabName = obj.getClass().getAnnotation(Table.class).value();	//��ȡ����
		params.put("TABLE_NAME", tabName);
		return baseMapper.queryCount(params);
	}	
}
