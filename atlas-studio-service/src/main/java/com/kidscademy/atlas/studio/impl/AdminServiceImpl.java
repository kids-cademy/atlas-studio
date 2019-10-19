package com.kidscademy.atlas.studio.impl;

import com.kidscademy.atlas.studio.AdminService;
import com.kidscademy.atlas.studio.dao.AdminDao;
import com.kidscademy.atlas.studio.model.Login;
import com.kidscademy.atlas.studio.model.User;

import js.tiny.container.core.AppContext;

public class AdminServiceImpl implements AdminService {
    private final AppContext context;
    private final AdminDao dao;

    public AdminServiceImpl(AppContext context, AdminDao dao) {
	this.context = context;
	this.dao = dao;
    }

    @Override
    public boolean login(Login login) {
	User user = dao.getUser(login);
	if (user != null) {
	    context.login(user);
	    return true;
	}
	return false;
    }

    @Override
    public boolean isAuthenticated() {
	return context.isAuthenticated();
    }

    @Override
    public void logout() {
	context.logout();
    }
}
