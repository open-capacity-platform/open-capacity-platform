package com.open.capacity.user.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.open.capacity.db.config.util.DataSourceHolder;
import com.open.capacity.db.config.util.DataSourceKey;
import com.open.capacity.user.dao.SysTes1tDao;
import com.open.capacity.user.dao.SysTest2Dao;

@Service
public class SysTestServce {
	@Resource
	private SysTes1tDao sysTes1tDao ;
	@Resource
	private SysTest2Dao sysTes2tDao ;
	
	@Transactional
	public void save1(){
		DataSourceHolder.setDataSourceKey(DataSourceKey.core);
		sysTes1tDao.save(null);
		 
		
	}
	@Transactional
	public void save2(){
 
	 
		DataSourceHolder.setDataSourceKey(DataSourceKey.log);
		sysTes2tDao.save(null);
		
	}
	@Transactional
	public void save(){
		 
		this.save1();
		System.out.println(1/0);
		this.save2();
		 
		
	}
	
}
