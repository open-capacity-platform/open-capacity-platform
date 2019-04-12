package com.open.capacity.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.dubbo.config.annotation.Reference;
import com.open.capacity.pojo.Product;
import com.open.capacity.service.ProductService;

  
@RestController
public class ProductController {
	
	//1.消费service服务层的接口
	@Reference(interfaceClass=ProductService.class,timeout=1200000,retries=0)
	private ProductService   productService;
	
	//2.消费业务层 
	@RequestMapping("/getListByPage")
	public List<Product> getProductList(){

		List<Product> list = productService.getProducts();
		
		return list; 
		   
	}
	 
		
	     
	 
}
