package com.kidscademy.atlas.studio.dao;

import com.kidscademy.atlas.studio.model.Login;
import com.kidscademy.atlas.studio.model.User;

public interface AdminDao {
    User getUser(Login login);

    User getUserById(int userId);
}
