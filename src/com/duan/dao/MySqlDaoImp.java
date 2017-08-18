package com.duan.dao;

import com.duan.bean.User;

public class MySqlDaoImp implements Dao{

	@Override
	public void add(User u) {
		System.out.println("我的mysql的add方法");
	}
	
}
