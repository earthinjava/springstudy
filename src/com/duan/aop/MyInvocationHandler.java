package com.duan.aop;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MyInvocationHandler implements InvocationHandler{
	
	private Object target;
	
	public void setTarget(Object target) {
		this.target = target;
	}
	public Object getProxy(){
		return Proxy.newProxyInstance(this.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {		
		System.out.println("Ö´ÐÐ·½·¨");
		method.invoke(target, args);		
		return null;
	}

}
