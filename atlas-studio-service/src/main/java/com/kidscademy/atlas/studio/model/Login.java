package com.kidscademy.atlas.studio.model;

public class Login
{
  private String emailAddress;
  private String password;

  public Login()
  {
  }

  /**
   * Test constructor.
   * 
   * @param emailAddress
   * @param password
   */
  public Login(String emailAddress, String password)
  {
    this.emailAddress = emailAddress;
    this.password = password;
  }

  public String getEmailAddress()
  {
    return emailAddress;
  }

  public String getPassword()
  {
    return password;
  }
}
