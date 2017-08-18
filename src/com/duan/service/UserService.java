package com.duan.service;

import com.duan.bean.User;
import com.duan.dao.Dao;

public class UserService {
	private Dao dao;	
	public void setDao(Dao dao) {
		this.dao = dao;
	}
	public Dao getDao() {
		return dao;
	}
	public void add(User u){
		dao.add(u);
	}
}
