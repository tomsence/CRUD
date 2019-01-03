package com.pm.entity;

import java.io.Serializable;

public class PageInfo implements Serializable{
	public static final long serialVersionUID = 1L;
	
	private int page;
	private int rows;
	private int firstPage;
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getFirstPage() {
		firstPage = (page - 1) * rows;
		return firstPage;
	}

}
