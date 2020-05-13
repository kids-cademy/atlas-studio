package com.kidscademy.atlas.studio.model;

import javax.persistence.Cacheable;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.security.auth.Subject;

import com.kidscademy.atlas.studio.dao.StringsArrayConverter;

import js.lang.RolesPrincipal;

@Entity
@Cacheable
public class User implements RolesPrincipal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String emailAddress;
    private String password;
    @Convert(converter = StringsArrayConverter.class)
    private String[] roles;

    public User() {
    }

    /**
     * Test constructor.
     * 
     * @param id
     */
    public User(int id) {
	this.id = id;
    }

    public int getId() {
	return id;
    }

    public String getEmailAddress() {
	return emailAddress;
    }

    public String getPassword() {
	return password;
    }

    @Override
    public String[] getRoles() {
	return roles;
    }

    @Override
    public String getName() {
	return emailAddress;
    }

    public boolean implies(Subject subject) {
	return false;
    }
}
