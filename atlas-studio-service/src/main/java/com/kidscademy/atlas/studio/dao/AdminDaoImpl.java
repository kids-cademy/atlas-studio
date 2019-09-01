package com.kidscademy.atlas.studio.dao;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.kidscademy.atlas.studio.model.Login;
import com.kidscademy.atlas.studio.model.User;

import js.transaction.Transactional;

@Transactional
public class AdminDaoImpl implements AdminDao {
    private final EntityManager em;

    public AdminDaoImpl(EntityManager em) {
	this.em = em;
    }

    @Override
    public User getUser(Login login) {
	String jpql = "select u from User u where u.emailAddress=?1 and u.password=?2";
	try {
	    return em.createQuery(jpql, User.class).setParameter(1, login.getEmailAddress())
		    .setParameter(2, login.getPassword()).getSingleResult();
	} catch (NoResultException unused) {
	    return null;
	}
    }

    @Override
    public User getUserById(int userId) {
	return em.find(User.class, userId);
    }
}
