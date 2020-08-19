package com.kidscademy.atlas.studio.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import js.lang.Displayable;
import js.tiny.container.annotation.TestConstructor;
import js.util.Strings;

@Entity
public class Fact implements Displayable
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String title;

  private String text;

  public Fact() {
  }

  public Fact(int id, String title, String text) {
    this.id = id;
    this.title = title;
    this.text = text;
  }

  @TestConstructor
  public Fact(String title, String text) {
    this.title = title;
    this.text = text;
  }

  public int getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public String getText() {
    return text;
  }

  @Override
  public String toDisplay() {
    return Strings.concat(title, ' ', text);
  }

  @Override
  public String toString() {
    return title;
  }
}
