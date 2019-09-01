package com.kidscademy.atlas.studio;

import com.kidscademy.atlas.studio.model.Login;

import js.annotation.Public;
import js.annotation.Service;

@Service
public interface AdminService {
    @Public
    boolean login(Login login);

    @Public
    boolean isAuthenticated();

    void logout();
}
