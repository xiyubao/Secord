package com.example.secord;

import javax.mail.Authenticator; 
import javax.mail.PasswordAuthentication; 
  
  
class PopupAuthenticator extends Authenticator { 
  private String userName; 
  private String password; 
  public PopupAuthenticator(String userName, String password) 
  { 
    this.userName = userName; 
    this.password = password; 
  } 
  public PasswordAuthentication getPasswordAuthentication() { 
    return new PasswordAuthentication(userName, password); 
  } 
} 