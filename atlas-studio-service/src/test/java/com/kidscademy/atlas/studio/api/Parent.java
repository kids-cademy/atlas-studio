package com.kidscademy.atlas.studio.api;

import java.util.List;

import javax.persistence.Cacheable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OrderColumn;

@Entity
@Cacheable(false)
class Parent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    @ElementCollection
    @OrderColumn
    private List<Child> children;

    public Parent() {
    }

    public Parent(String name) {
	this.name = name;
    }

    public String getName() {
	return name;
    }

    public void setName(String name) {
	this.name = name;
    }

    public List<Child> getChildren() {
	return children;
    }

    public void setChildren(List<Child> children) {
	this.children = children;
    }

    public int getId() {
	return id;
    }
}