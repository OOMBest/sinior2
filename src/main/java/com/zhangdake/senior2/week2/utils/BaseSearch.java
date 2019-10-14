package com.zhangdake.senior2.week2.utils;

/**
 * Author:DUCK
 * Creation time:2019-7-25 下午7:40:22
 * Describe:基础查询搜索封装类 仅封装了分页属性
 * History:1.0
 */
public class BaseSearch {

	private Integer currentPage;
	private Pagination pagination;
	
	public BaseSearch() {}
	
	public Integer getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(Integer currentPage) {
		this.currentPage = currentPage;
	}

	public Pagination getPagination() {
		return pagination;
	}
	
	public void createPagination(int count, int pageSize) {
		this.pagination = new Pagination(currentPage, count, pageSize);
	}
	
	public void createPagination(int count) {
		this.pagination = new Pagination(currentPage, count);
	}

	@Override
	public String toString() {
		return "BaseSearch [currentPage=" + currentPage + "]";
	}
	
}
