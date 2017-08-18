package com.duan.test;

import com.duan.aop.MyInvocationHandler;
import com.duan.bean.User;
import com.duan.dao.Dao;
import com.duan.spring.BeanFactory;
import com.duan.spring.ClassPathXMLApplicationContext;

public class Test02 {
	public static void main(String[] args) {
		
		BeanFactory factory=new ClassPathXMLApplicationContext("beans.xml");		
		User user = (User) factory.getBean("User");
		MyInvocationHandler m1=(MyInvocationHandler) factory.getBean("Handler1");
		MyInvocationHandler m2=(MyInvocationHandler) factory.getBean("Handler2");
		
		
		
		Dao proxy = (Dao) m1.getProxy();
		proxy.add(user);
		
		Dao proxy2 = (Dao) m2.getProxy();
		proxy2.add(user);
	}
}
