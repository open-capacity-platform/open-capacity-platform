package com.open.capacity.user.dao;

import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
/**
* @author 作者 owen E-mail: 624191343@qq.com
* @version 创建时间：2017年11月13日 上午22:57:51
 * 菜单
 */
@Mapper
public interface SysTes1tDao {

	@Insert("insert into sys_test(username ) "
			+ "values ('test' )")
	int save(Map<String,String> params);

	 
}
