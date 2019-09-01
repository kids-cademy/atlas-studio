package com.kidscademy.atlas.studio.it;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.kidscademy.atlas.studio.dao.AdminDao;
import com.kidscademy.atlas.studio.dao.AdminDaoImpl;
import com.kidscademy.atlas.studio.model.Login;
import com.kidscademy.atlas.studio.model.User;

import js.transaction.TransactionFactory;
import js.transaction.eclipselink.TransactionFactoryImpl;
import js.unit.db.Database;
import js.util.Classes;

public class AdminDaoTest {
    private static Database database;
    private static TransactionFactory factory;

    @BeforeClass
    public static void beforeClass() throws IOException {
	database = new Database("atlas_test", "kids_cademy", "kids_cademy");
	factory = new TransactionFactoryImpl();
    }

    private AdminDao dao;

    @Before
    public void beforeTest() throws SQLException {
	database.load(Classes.getResourceAsStream("admin-data-set.xml"));
	dao = factory.newInstance(AdminDaoImpl.class);
    }

    @Test
    public void getUser() {
	Login login = new Login("john.doe@email.com", "secret");
	User user = dao.getUser(login);

	assertThat(user, notNullValue());
	assertThat(user.getId(), not(equalTo(0)));
	assertThat(user.getEmailAddress(), equalTo(login.getEmailAddress()));
	assertThat(user.getName(), equalTo(login.getEmailAddress()));
	assertThat(user.getPassword(), equalTo(login.getPassword()));
    }
}
