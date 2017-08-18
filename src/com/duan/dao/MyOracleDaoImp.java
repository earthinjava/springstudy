package com.duan.dao;

import com.duan.bean.User;

public class MyOracleDaoImp implements Dao{

	@Override
	public void add(User u) {
		System.out.println("我的Oracle的add方法");
	}
}
