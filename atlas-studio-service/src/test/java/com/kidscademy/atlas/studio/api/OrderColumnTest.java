package com.kidscademy.atlas.studio.api;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import js.unit.db.Database;
import js.util.Classes;

/**
 * Prove behavior for OrderColumn annotation.
 * 
 * @author Iulian Rotaru
 */
public class OrderColumnTest {
    private static Database database;
    private static EntityManagerFactory factory;
    private static EntityManager em;

    @BeforeClass
    public static void beforeClass() {
	database = new Database("kids_cademy_poc", "kids_cademy", "kids_cademy");
	factory = Persistence.createEntityManagerFactory("api");
    }

    @AfterClass
    public static void afterClass() {
	factory.close();
    }

    @Before
    public void beforeTest() throws SQLException {
	database.load(Classes.getResourceAsStream("api-data-set.xml"));
    }

    @Test
    public void loadParent() {
	em = factory.createEntityManager();
	String jpql = "select p from Parent p where p.id=:id";
	Parent parent = em.createQuery(jpql, Parent.class).setParameter("id", 1).getSingleResult();
	em.close();

	assertThat(parent, notNullValue());
	assertThat(parent.getId(), equalTo(1));
	assertThat(parent.getName(), equalTo("parent"));

	List<Child> children = parent.getChildren();
	assertThat(children, notNullValue());
	assertThat(children, hasSize(3));
	assertThat(children.get(0).getName(), equalTo("child 1"));
	assertThat(children.get(1).getName(), equalTo("child 0"));
	assertThat(children.get(2).getName(), equalTo("child 2"));
    }

    /**
     * Load children list directly with JPQL, that is, not as part of its parent.
     * Although generated SQL retrieves children_order, returned list is ordered by
     * id column. This is in contrast with loading children list via parent, where
     * list is ordered by children_order column.
     * <p>
     * Result is somehow surprising: why JPA implementation retrieves children_order
     * column if ignore it? Since children_order is part of generated SQL it is
     * clear that implementation knows about parent {@literal}OrderColum annotation,
     * but actively choose not to use it.
     * <p>
     * APIDOC does not state something about this condition. Answer on SO consider
     * this condition as normal.
     */
    @Test
    public void loadChildren() {
	em = factory.createEntityManager();
	//String jpql = "select p.children from Parent p where p.id=:id";
	String jpql = "select c from Parent p join p.children c where p.id=:id";
	List<Child> children = em.createQuery(jpql, Child.class).setParameter("id", 1).getResultList();
	em.close();

	assertThat(children, notNullValue());
	assertThat(children, hasSize(3));

	// I would expect the same result as on loadParent test case
	// assertThat(children.get(0).getName(), equalTo("child 1"));
	// assertThat(children.get(1).getName(), equalTo("child 0"));
	// assertThat(children.get(2).getName(), equalTo("child 2"));

	// returned list does not use children_order column and return database ID order
	assertThat(children.get(0).getName(), equalTo("child 0"));
	assertThat(children.get(1).getName(), equalTo("child 1"));
	assertThat(children.get(2).getName(), equalTo("child 2"));
    }

    @Test
    public void loadChild() {
	em = factory.createEntityManager();
	String jpql = "select c from Parent p join p.children c where p.id=:id and index(c)=0";
	Child child = em.createQuery(jpql, Child.class).setParameter("id", 1).getSingleResult();
	em.close();
	
	assertThat(child, notNullValue());
	assertThat(child.getName(), equalTo("child 1"));
    }
    
    /** Merge reorder of list annotated with OrderColumn. */
    @Test
    public void mergeReorderedList() {
	Parent parent = new Parent("Parent");

	List<Child> children = new ArrayList<>();
	parent.setChildren(children);

	children.add(new Child("Child 0"));
	children.add(new Child("Child 1"));
	children.add(new Child("Child 2"));

	em = factory.createEntityManager();
	EntityTransaction tx = em.getTransaction();
	tx.begin();
	em.persist(parent);
	tx.commit();
	em.close();

	String jpql = "select p from Parent p where p.id=:id";
	em = factory.createEntityManager();
	Parent persistedParent = em.createQuery(jpql, Parent.class).setParameter("id", parent.getId())
		.getSingleResult();
	em.close();

	children = persistedParent.getChildren();
	assertThat(children.get(0).getName(), equalTo("Child 0"));
	assertThat(children.get(1).getName(), equalTo("Child 1"));
	assertThat(children.get(2).getName(), equalTo("Child 2"));

	Collections.swap(parent.getChildren(), 0, 1);
	em = factory.createEntityManager();
	tx = em.getTransaction();
	tx.begin();
	em.merge(parent);
	tx.commit();
	em.close();

	em = factory.createEntityManager();
	persistedParent = em.createQuery(jpql, Parent.class).setParameter("id", parent.getId()).getSingleResult();
	em.close();

	children = persistedParent.getChildren();
	assertThat(children.get(0).getName(), equalTo("Child 1"));
	assertThat(children.get(1).getName(), equalTo("Child 0"));
	assertThat(children.get(2).getName(), equalTo("Child 2"));
    }
}
