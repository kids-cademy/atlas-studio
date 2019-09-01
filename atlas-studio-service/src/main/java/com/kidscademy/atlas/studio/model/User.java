package com.kidscademy.atlas.studio.model;

import java.security.Principal;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.security.auth.Subject;

@Entity
@Cacheable
public class User implements Principal
{
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;

  private String emailAddress;
  private String password;

  public User()
  {
  }

  /**
   * Test constructor.
   * 
   * @param id
   */
  public User(int id)
  {
    this.id = id;
  }

  public int getId()
  {
    return id;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public String getPassword()
  {
    return password;
  }

  @Override
  public String getName()
  {
    return emailAddress;
  }

  public boolean implies(Subject subject)
  {
    return false;
  }
}
