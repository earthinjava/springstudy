package com.duan.test;

import com.duan.bean.User;
import com.duan.service.UserService;
import com.duan.spring.BeanFactory;
import com.duan.spring.ClassPathXMLApplicationContext;

public class Test {
	public static void main(String[] args) {	
		
		BeanFactory factory=new ClassPathXMLApplicationContext("beans.xml");
		User user=(User) factory.getBean("User");
		UserService service=(UserService) factory.getBean("UserService");		
		service.add(user);		
	}
}
