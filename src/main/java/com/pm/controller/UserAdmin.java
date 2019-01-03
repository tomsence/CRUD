package com.pm.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.pm.entity.PageInfo;
import com.pm.entity.UserInfo;
import com.pm.service.BaseServiceClientImpl;

import net.sf.json.JSONObject;

@Controller
public class UserAdmin {
	@Autowired
	private BaseServiceClientImpl baseServiceClientImpl;
	
	/*增加用户*/
	@RequestMapping(value="/user/add", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userAdd(@RequestBody UserInfo userinfo) {
		Map<String,Object> resultMap = new HashMap<String,Object>();
		int iRet = -1;
		List<Object> userList = baseServiceClientImpl.selectAll(userinfo);
		if(userList.size() > 0) {
			resultMap.put("stauts",Boolean.FALSE);
			resultMap.put("message", "用户名已存在,请重新添加 !");
		}else {
			iRet = baseServiceClientImpl.insert(userinfo);
			if(iRet == -1)
			{
				resultMap.put("stauts",Boolean.FALSE);
				resultMap.put("message", "数据操作错误!");
			}else
			{
				resultMap.put("status", Boolean.TRUE);
				resultMap.put("message", "添加成功!");
			}
		}
		return resultMap;
	}
	
	/*修改用户信息*/
	@RequestMapping(value="/user/mod", method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> userMod(@RequestBody UserInfo userinfo ) {
		int iRet = -1;
		Map<String, Object> ResultMap = new HashMap<String,Object>();
		HashMap<String, UserInfo> userMap = baseServiceClientImpl.select(userinfo.getId(), userinfo.getClass());
		System.out.println("根据id["+userinfo.getId()+"]取到的值为:" + userMap );
		if(userMap.size()>0) {
			/*修改数据*/
			iRet = baseServiceClientImpl.update(userinfo);
			System.out.println("====="+ iRet+ "===========");
			if(iRet == 1) {
				ResultMap.put("status", Boolean.TRUE);
				ResultMap.put("message", "修改成功!");
			}else {
				ResultMap.put("status", Boolean.FALSE);
				ResultMap.put("message", "修改信息失败!");
			}
		}else {
			ResultMap.put("status", Boolean.FALSE);
			ResultMap.put("message", "未找到用户信息!");
		}
		return ResultMap;
	}
	
	/*删除用户信息*/
	@RequestMapping(value="/user/del", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> userDel(@RequestParam("userName")String userName){
		System.out.println(userName);
		int iRet = -1;
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = new UserInfo();
		userInfo.setUserName(userName);
		
		List<Object> userList = baseServiceClientImpl.selectAll(userInfo);
		Map<String, Object> userMap = (Map<String, Object>) userList.get(0);
		userInfo.setId((int) userMap.get("id"));
		System.out.println("删除前userinfo的值为:" + userInfo);
		iRet = baseServiceClientImpl.delete(userInfo);
		if(iRet == 1) {
			System.out.println("del!!!!!" + iRet);
			resultMap.put("status", Boolean.TRUE);
			resultMap.put("message", "删除成功!");
		}else {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("message", "未找到用户信息!");
		}
			
		return resultMap;
	}
	
	/*附件上传用户*/
	@RequestMapping(value="/user/upload",method=RequestMethod.POST)
	@ResponseBody
	public Map<String, Object> uploadUser(@RequestParam("contractFile")MultipartFile file) throws IOException{
		Map<String, Object> resultMap = new HashMap<String,Object>();
		System.out.println("文件名为:" + file.getOriginalFilename());
		List<UserInfo> userInfos =  new ArrayList<UserInfo>();
		Workbook wb = null;
		String filename = file.getOriginalFilename();
		String prefix = filename.substring(filename.lastIndexOf('.')+1);
		if(prefix.equals("xlsx")) {
			wb = new XSSFWorkbook(file.getInputStream());
			System.out.println("文件后缀名为:" + prefix);
		}else if(prefix.equals("xls")) {
			wb = new HSSFWorkbook(file.getInputStream());
		}else {
			resultMap.put("status", Boolean.FALSE);
			resultMap.put("message", "不可识别的文件类型!");
			return resultMap;
		}
		
		Sheet sheet1 = wb.getSheetAt(0);
		System.out.println("11111111111111111");
		for(org.apache.poi.ss.usermodel.Row row:sheet1) {
			System.out.println("2222222222222222");
			int i = 1;
			UserInfo userInfo = new UserInfo();
			for(Cell cell:row) {
				String data = getStringCellValue(cell);
				System.out.println("++++++"+data+"++++++++");
				switch (i++) {
				case 1:
					userInfo.setUserName(data);
					break;
				case 2:
					userInfo.setUserPasswd(data);;
					break;
				case 3:
					userInfo.setEmail(data);;
					break;
				case 4:
					userInfo.setTelephone(data);;
					break;
				case 5:
					userInfo.setStatus(data);;
					break;
				case 6:
					userInfo.setRemark(data);;
					break;
				default:
					break;
				}
			}
			int iRet = -1;
			iRet = baseServiceClientImpl.insert(userInfo);
			if(iRet == -1)
			{
				resultMap.put("stauts",Boolean.FALSE);
				resultMap.put("message", "数据操作错误!");
			}else
			{
				resultMap.put("status", Boolean.TRUE);
				resultMap.put("message", "添加成功!");
			}
		}
		
		resultMap.put("filename", file.getOriginalFilename());
		return resultMap;
	}
	
	public String getStringCellValue(Cell cell) {
        String strCell;
        switch (cell.getCellType()) {
            case HSSFCell.CELL_TYPE_STRING:
                strCell = cell.getStringCellValue();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                strCell = String.valueOf((int)cell.getNumericCellValue());
                break;
            case HSSFCell.CELL_TYPE_BOOLEAN:
                strCell = String.valueOf(cell.getBooleanCellValue());
                break;
            case HSSFCell.CELL_TYPE_BLANK:
                strCell = "";
                break;
            default:
                strCell = "";
                break;
        }
        if (strCell.equals("")) {
            return "";
        }
        return strCell;
    }
	
	/*查询所有用户*/	
	@RequestMapping(value="/user/reload", method=RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> loadAllUser(PageInfo pageinfo) {
		System.out.println("页码" + pageinfo.getPage());
		System.out.println("行数" + pageinfo.getRows());
		Map<String, Object> resultMap = new HashMap<String, Object>();
		UserInfo userInfo = new UserInfo();
		List<Object> userList = baseServiceClientImpl.selectPageAll(userInfo, pageinfo.getFirstPage(), pageinfo.getRows());
		for(Object l:userList) {
			System.out.println("测试输出：" + l);
		}
		if(userList.size()<=0) {
			resultMap.put("status",Boolean.FALSE);
			resultMap.put("message", "没有查询到数据");
		}else {
			resultMap.put("status",Boolean.TRUE);
			resultMap.put("message", "查询成功!");
			resultMap.put("rows", userList);
			resultMap.put("total",baseServiceClientImpl.queryCount(userInfo));
		}
		return resultMap;
	}	
}
