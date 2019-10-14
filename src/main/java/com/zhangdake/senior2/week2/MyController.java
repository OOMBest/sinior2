package com.zhangdake.senior2.week2;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.zhangdake.senior2.week2.utils.BaseSearch;

import cn.com.zhangdake.utils.JsonMananger;


/**
 * Author: ZhangDaKe
 * Date: 2019年10月14日
 * Describe: 
 * Version: 1.0
 */

@Controller
public class MyController {

	private final String LIST_TAG = "goods_list";
	private final String ZSET_TAG = "goods_zset";
	
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	private MyController() {}
	
	@RequestMapping("redisList")
	public String redisList(Model m, Integer currentPage) {
		BaseSearch bs = new BaseSearch();
		bs.setCurrentPage(currentPage == null ? 1 : currentPage);
		int size = redisTemplate.opsForList().size(LIST_TAG).intValue();
		bs.createPagination(size);
		List<Goods> pageList = getPageList(redisTemplate, LIST_TAG, bs.getCurrentPage(), 
				bs.getPagination().getPageSize(), Goods.class, true);
		m.addAttribute("list", pageList);
		m.addAttribute("page", bs);
		return "redisList";
	}
	
	@RequestMapping("redisZSet")
	public String redisZSet(Model m, Integer currentPage) {
		BaseSearch bs = new BaseSearch();
		bs.setCurrentPage(currentPage == null ? 1 : currentPage);
		int size = redisTemplate.opsForZSet().size(ZSET_TAG).intValue();
		bs.createPagination(size);
		List<Goods> pageList = getPageZSet(redisTemplate, ZSET_TAG, bs.getCurrentPage(), 
				bs.getPagination().getPageSize(), Goods.class, true);
		m.addAttribute("set", pageList);
		m.addAttribute("page", bs);
		return "redisZSet";
	}
	
	/**
	 * forList
	 * 
	 * @param rt 为key和 json类型的
	 * @param key redis中集合的key
	 * @param startPage 当前页数
	 * @param pageSize 每页数量
	 * @param cls 返回的对象类型
	 * @param order true从集合首部按顺序获取 false从集合尾部按顺序获取
	 * @return
	 */
	public static <T> List<T> getPageList(RedisTemplate<String, String> rt, String key,
			int startPage, int pageSize, Class<T> cls, boolean order) {
		//起始索引
		int startIndex = (startPage - 1) * pageSize;

		//redis中key的数据长度  长度不会太长  用不到Long 强转不会有损失
		int size = rt.opsForList().size(key).intValue();

		//页数超出内存条数范围
		if (size < startIndex) {
			return null;
		}

		int start; //查询的起始索引
		int end;//查询的结束索引

		if (order) { //正序获取
			start = startIndex;
			end = start + pageSize > size ? size - 1 : start + pageSize - 1;
		} else { //倒序获取
			end = size - startIndex;
			start = end - pageSize < 0 ? 0 : end - pageSize;
		}

		List<String> list = rt.opsForList().range(key, start, end);

		int resultSize = list.size();
		List<T> testList = new ArrayList<T>(resultSize);
		T t;
		try {
			if (order) {
				for (int i = 0; i < resultSize; i++) {
					t = JsonMananger.jsonToBean(list.get(i), cls);
					testList.add(t);
				}
			} else {
				for (int i = resultSize - 1; i > -1; i--) {
					t = JsonMananger.jsonToBean(list.get(i), cls);
					testList.add(t);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return testList;
	}
	
	/**
	 * forZSet
	 * 
	 * @param rt 为key和 json类型的
	 * @param key redis中集合的key
	 * @param startPage 当前页数
	 * @param pageSize 每页数量
	 * @param cls 返回的对象类型
	 * @param order true从集合首部按顺序获取 false从集合尾部按顺序获取
	 * @return
	 */
	public static <T> List<T> getPageZSet(RedisTemplate<String, String> rt, String key,
			int startPage, int pageSize, Class<T> cls, boolean order) {
		//起始索引
		int startIndex = (startPage - 1) * pageSize;

		//redis中key的数据长度  长度不会太长  用不到Long 强转不会有损失
		int size = rt.opsForZSet().size(key).intValue();

		//页数超出内存条数范围
		if (size < startIndex) {
			return null;
		}

		int start; //查询的起始索引
		int end;//查询的结束索引

		if (order) { //正序获取
			start = startIndex;
			end = start + pageSize > size ? size - 1 : start + pageSize - 1;
		} else { //倒序获取
			end = size - startIndex;
			start = end - pageSize < 0 ? 0 : end - pageSize;
		}

		Set<String> set = rt.opsForZSet().reverseRange(key, start, end);

		int resultSize = set.size();
		List<T> testList = new ArrayList<T>(resultSize);
		T t;
		try {
			if (order) {
				Iterator<String> iterator = set.iterator();
				for (;iterator.hasNext();) {
					t = JsonMananger.jsonToBean(iterator.next(), cls);
					testList.add(t);
				}
			} else {
				Iterator<String> iterator = set.iterator();
				for (;iterator.hasNext();) {
					t = JsonMananger.jsonToBean(iterator.next(), cls);
					testList.add(0, t);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return testList;
	}
	
}


