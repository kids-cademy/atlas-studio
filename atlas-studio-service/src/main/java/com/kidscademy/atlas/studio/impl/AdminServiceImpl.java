package com.kidscademy.atlas.studio.impl;

import com.kidscademy.atlas.studio.AdminService;
import com.kidscademy.atlas.studio.dao.AdminDao;
import com.kidscademy.atlas.studio.model.Login;

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
	return context.login(login.getEmailAddress(), login.getPassword());
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
