package com.zhangdake.senior2.week2.utils;

/**
 * Time : 2018-3-30 下午2:10:08 
 * Author : 许文雄
 * Description : 分页工具类
 */
public final class Pagination {

	private final int currentPage; //当前页
	private final int prevPage; //上一页
	private final int nextPage; //下一页
	private final int lastPage; //尾页
	private final int count; //总条数
	private final int pageSize; //每页展示的数据条数
	private final int pageRecord; //pageRecord
	private final String paginationTab; //底部 上一页 下一页的html文本标签
	
	/**
	 * 每页展示条数 使用默认设置
	 * 
	 * @param currentPage 当前页
	 * @param count 总条数
	 */
	public Pagination(Integer currentPage, int count) {
		this(currentPage, count, 0);
	}
	
	/**
	 * @param currentPage 当前页
	 * @param count 总条数
	 * @param pageSize 每页展示的数据条数
	 */
	public Pagination(Integer currentPage, int count, int pageSize) {
		this.count = count < 0 ? 0 : count;
		this.pageSize = pageSize <= 0 ? 10 : pageSize;
		this.lastPage = this.count % this.pageSize == 0 ? this.count / this.pageSize : this.count / this.pageSize + 1;
		
		 //没有传值进来  多为第一次请求  this.lastPage = 0时 为没有数据 或 数据删除没了
		if (currentPage == null || currentPage <= 0 || this.lastPage == 0) {
			this.currentPage = 1;
		} else if (currentPage > this.lastPage) { //当前页比尾页大 强制等于尾页 可能为删除了一页的数据
			this.currentPage = this.lastPage;
		} else { //other
			this.currentPage = currentPage;
		}
		
		this.prevPage = this.currentPage == 1 ? this.currentPage : this.currentPage - 1;
		this.nextPage = this.currentPage == this.lastPage ? this.lastPage : this.currentPage + 1;
		this.pageRecord = (this.currentPage - 1) * this.pageSize;
		this.paginationTab = initPaginationTab();
	}

	/**
	 * 生成底部 上一页 下一页的html文本标签
	 * 
	 * @return 自动生成的html文本标签
	 */
	private String initPaginationTab() {
		StringBuilder bottomPage = new StringBuilder(128);
		bottomPage.append("<button onclick='pagination(1)'>首页</button>&nbsp;");
		bottomPage.append("<button onclick='pagination(").append(prevPage).append(")'>上一页</button>&nbsp;");
		bottomPage.append("第").append(currentPage).append("/").append(lastPage)
				  .append("页，共").append(count).append("条数据&nbsp;");
		bottomPage.append("<button onclick='pagination(").append(nextPage).append(")'>下一页</button>&nbsp;");
		bottomPage.append("<button onclick='pagination(").append(lastPage).append(")'>尾页</button>");
		return bottomPage.toString();
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPrevPage() {
		return prevPage;
	}

	public int getNextPage() {
		return nextPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public int getCount() {
		return count;
	}

	public int getPageSize() {
		return pageSize;
	}

	public int getPageRecord() {
		return pageRecord;
	}

	public String getPaginationTab() {
		return paginationTab;
	}

}
