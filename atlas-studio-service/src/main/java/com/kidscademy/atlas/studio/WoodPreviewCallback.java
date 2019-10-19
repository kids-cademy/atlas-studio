package com.kidscademy.atlas.studio;

import com.kidscademy.atlas.studio.dao.AdminDao;
import com.kidscademy.atlas.studio.model.User;

import js.lang.Callback;
import js.tiny.container.core.AppContext;

public class WoodPreviewCallback implements Callback<AppContext>
{
  @Override
  public void handle(AppContext context)
  {
    AdminDao dao = context.getInstance(AdminDao.class);
    User user = dao.getUserById(1);
    context.login(user);
  }
}
