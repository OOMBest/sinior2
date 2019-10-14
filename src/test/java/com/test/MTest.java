package com.test;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.zhangdake.senior2.week2.Goods;

import cn.com.zhangdake.utils.FileUtils;
import cn.com.zhangdake.utils.JsonMananger;
import cn.com.zhangdake.utils.ReflectionUtils;
import cn.com.zhangdake.utils.port.FieldFilter;
import cn.com.zhangdake.utils.port.ObjectCallBack;
import cn.com.zhangdake.utils.port.impl.SimpleFieldFilter;

/**
 * Author: ZhangDaKe
 * Date: 2019年10月14日
 * Describe:
 * Version: 1.0
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:redis.xml")
public class MTest {

	private final String LIST_TAG = "goods_list";
	private final String ZSET_TAG = "goods_zset";

	@Autowired
	private RedisTemplate<String, String> redisTemplate;

	@Test
	public void inputGoods() {

		redisTemplate.delete(LIST_TAG);
		redisTemplate.delete(ZSET_TAG);

		//获取资源路径
		String url = FileUtils.getRootDirectory(MTest.class) + "good1.txt";
		FieldFilter filter = new SimpleFieldFilter();
		Object[] params = new Object[4];

		List<Goods> list = FileUtils.readFileToObject(new File(url),
				new ObjectCallBack<Goods>() {

					@Override
					public Goods getObject(String line) {
						String[] arr = line.split("==");

						//判断id值是不是数字
						if (!isNumber(arr[0])) {
							//nothing
						}

						//判断商品名称有没有值
						if (!hasText(arr[1])) {
							//nothing
						}

						//判断价格有没有值
						if (!hasText(arr[2])) {
							//nothing
						}

						//判断价格是否是数字 不是数字进行转换
						arr[2] = arr[2].replace("￥", "");
						if (!isNumber(arr[2])) {
							arr[2] = arr[2].split(" ")[0];
						}

						//判断百分比是否有值 没有值赋默认值
						if (arr.length > 3) {
							arr[3] = arr[3].replace("%", "");
						} else {
							params[3] = 0;
						}

						//将设置好的值 转换给bean 并进行赋值
						for (int i = 0; i < arr.length; i++) {
							params[i] = arr[i];
						}

						return ReflectionUtils.setObject(new Goods(), params, filter);
					}
				});

		//倒序的redis list集合
		String json;
		for (Goods goods : list) {
			json = JsonMananger.beanToJson(goods);
			redisTemplate.opsForList().leftPush(LIST_TAG, json);
			redisTemplate.opsForZSet().add(ZSET_TAG, json, goods.getSoldOut());
		}

		//按已售百分比倒序排序
		Collections.sort(list, new Comparator<Goods>() {

			@Override
			public int compare(Goods o1, Goods o2) {
				return o2.getSoldOut() - o1.getSoldOut();
			}
		});
		
		Set<String> set = redisTemplate.opsForZSet().reverseRange(ZSET_TAG, 0, 9);
		Iterator<String> iterator = set.iterator();
		while (iterator.hasNext()) {
			String m = iterator.next();
			System.out.println(m);
		}
		
	}

	public static boolean isNumber(String str) {
		return str.matches("[0-9]*");
	}

	private static boolean hasText(String text) {
		return StringUtils.isEmpty(text);
	}

}
