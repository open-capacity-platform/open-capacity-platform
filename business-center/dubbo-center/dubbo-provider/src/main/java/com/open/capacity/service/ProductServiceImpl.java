package com.open.capacity.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.alibaba.dubbo.config.annotation.Service;
import com.open.capacity.pojo.Product;


 
@Service(interfaceClass=ProductService.class,timeout=1200000,retries=0)
@Component
public class ProductServiceImpl   implements ProductService  {

	@Override
	public List<Product> getProducts() {
		// TODO Auto-generated method stub
		List<Product>  list = new ArrayList<>();
		
		for(int i = 1 ; i <10 ; i++){
			Product prod = new Product();
			
			prod.setId(UUID.randomUUID().toString());
			prod.setRemark("prod" + i );
			list.add(prod) ;
		}
		
		return list;
	}
 
}
