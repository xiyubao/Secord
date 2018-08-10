package com.example.secord;

import java.util.Date; 
import java.util.Properties; 
  
import javax.activation.DataHandler; 
import javax.activation.DataSource; 
import javax.activation.FileDataSource; 
import javax.mail.Address; 
import javax.mail.Message; 
import javax.mail.Multipart; 
import javax.mail.Session; 
import javax.mail.Transport; 
import javax.mail.internet.InternetAddress; 
import javax.mail.internet.MimeBodyPart; 
import javax.mail.internet.MimeMessage; 
import javax.mail.internet.MimeMultipart; 
  
public class EMailSender { 
  
  private String host; 
  private String port; 
  private String userName; 
  private String password; 
  private String images; 
  
  public String getImagePath() { 
    return images; 
  } 
  
  public void setImagePath(String string) { 
    this.images = string; 
  } 
  
  public EMailSender(String host, String port, String userName, String password)  
  { 
    this.host = host; 
    this.port = port; 
    this.userName = userName; 
    this.password = password; 
  } 
  
  public void sendEmail(String subject, String recepits, String sender, String content)  
  { 
    Properties props = new Properties(); 
    props.put("mail.smtp.host", host); //设置smtp的服务器地址 
    // props.put("mail.smtp.starttls.enable", "true"); 
    // props.put("mail.smtp.port", port); // 设置端口 
    // props.put("mail.smtp.auth", "true"); //设置smtp服务器要身份验证。 
      
    props.put("mail.smtp.socketFactory.port", port); 
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
    props.put("mail.smtp.auth", "true"); 
    props.put("mail.smtp.port", port); 
      
    // 返回授权Base64编码 
    PopupAuthenticator auth = new PopupAuthenticator(userName, password); 
    // 获取会话对象 
    Session session = Session.getInstance(props, auth);  
    // 设置为DEBUG模式 
    session.setDebug(true); 
      
    // 邮件内容对象组装 
    MimeMessage message = new MimeMessage(session); 
    try
    { 
      Address addressFrom = new InternetAddress(sender, "Jia Zhi Gang"); 
      Address addressTo = new InternetAddress(recepits, "My QQ E-Mail"); 
      message.setSubject(subject); 
      message.setSentDate(new Date()); 
      message.setFrom(addressFrom); 
      message.addRecipient(Message.RecipientType.TO,addressTo); 
         
      // 邮件文本/HTML内容 
      Multipart multipart = new MimeMultipart(); 
      MimeBodyPart messageBodyPart = new MimeBodyPart(); 
      messageBodyPart.setContent(content, "text/html"); 
      multipart.addBodyPart(messageBodyPart); 
        
      // 添加邮件附件 
      if (images != null && images.length() > 0) { 
    	  String filePath = images;
    	  
        //for (String filePath : images) { 
          MimeBodyPart attachPart = new MimeBodyPart();   
          DataSource source = new FileDataSource(filePath); 
          attachPart.setDataHandler(new DataHandler(source)); 
          attachPart.setFileName(filePath); 
          multipart.addBodyPart(attachPart); 
        //} 
      } 
  
      // 保存邮件内容 
      message.setContent(multipart); 
        
      // 获取SMTP协议客户端对象，连接到指定SMPT服务器 
      Transport transport = session.getTransport("smtp"); 
      transport.connect(host, Integer.parseInt(port), userName, password); 
      System.out.println("connet it success!!!!"); 
        
      // 发送邮件到SMTP服务器 
      Thread.currentThread().setContextClassLoader( getClass().getClassLoader() ); 
      Transport.send(message); 
      System.out.println("send it success!!!!"); 
        
      // 关闭连接 
      transport.close(); 
    } 
    catch(Exception e) 
    { 
      e.printStackTrace(); 
    } 
  } 
  
  public String getHost() { 
    return host; 
  } 
  
  public void setHost(String host) { 
    this.host = host; 
  } 
  
  public String getPort() { 
    return port; 
  } 
  
  public void setPort(String port) { 
    this.port = port; 
  } 
  
  public String getUserName() { 
    return userName; 
  } 
  
  public void setUserName(String userName) { 
    this.userName = userName; 
  } 
  
  public String getPassword() { 
    return password; 
  } 
  
  public void setPassword(String password) { 
    this.password = password; 
  } 
  
} 