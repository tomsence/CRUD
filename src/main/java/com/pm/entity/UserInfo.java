package com.pm.entity;

import java.io.Serializable;

import com.pm.annotation.Column;
import com.pm.annotation.Id;
import com.pm.annotation.Table;

@Table(value="pm_user_info")
public class UserInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private int id;
	private String userName;
	private String userPasswd;
	private String email;
	private String telephone;
	private String status;
	private String remark;
	
	@Id(value="id")
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	@Column(value="user_name",key="Y")
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	@Column(value="user_passwd",key="N")
	public String getUserPasswd() {
		return userPasswd;
	}
	public void setUserPasswd(String userPasswd) {
		this.userPasswd = userPasswd;
	}
	
	@Column(value="email",key="N")
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	@Column(value="telephone",key="N")
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	
	@Column(value="status",key="N")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(value="remark",key="N")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
