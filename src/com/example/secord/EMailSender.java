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
    props.put("mail.smtp.host", host); //����smtp�ķ�������ַ 
    // props.put("mail.smtp.starttls.enable", "true"); 
    // props.put("mail.smtp.port", port); // ���ö˿� 
    // props.put("mail.smtp.auth", "true"); //����smtp������Ҫ�����֤�� 
      
    props.put("mail.smtp.socketFactory.port", port); 
    props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); 
    props.put("mail.smtp.auth", "true"); 
    props.put("mail.smtp.port", port); 
      
    // ������ȨBase64���� 
    PopupAuthenticator auth = new PopupAuthenticator(userName, password); 
    // ��ȡ�Ự���� 
    Session session = Session.getInstance(props, auth);  
    // ����ΪDEBUGģʽ 
    session.setDebug(true); 
      
    // �ʼ����ݶ�����װ 
    MimeMessage message = new MimeMessage(session); 
    try
    { 
      Address addressFrom = new InternetAddress(sender, "Jia Zhi Gang"); 
      Address addressTo = new InternetAddress(recepits, "My QQ E-Mail"); 
      message.setSubject(subject); 
      message.setSentDate(new Date()); 
      message.setFrom(addressFrom); 
      message.addRecipient(Message.RecipientType.TO,addressTo); 
         
      // �ʼ��ı�/HTML���� 
      Multipart multipart = new MimeMultipart(); 
      MimeBodyPart messageBodyPart = new MimeBodyPart(); 
      messageBodyPart.setContent(content, "text/html"); 
      multipart.addBodyPart(messageBodyPart); 
        
      // ����ʼ����� 
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
  
      // �����ʼ����� 
      message.setContent(multipart); 
        
      // ��ȡSMTPЭ��ͻ��˶������ӵ�ָ��SMPT������ 
      Transport transport = session.getTransport("smtp"); 
      transport.connect(host, Integer.parseInt(port), userName, password); 
      System.out.println("connet it success!!!!"); 
        
      // �����ʼ���SMTP������ 
      Thread.currentThread().setContextClassLoader( getClass().getClassLoader() ); 
      Transport.send(message); 
      System.out.println("send it success!!!!"); 
        
      // �ر����� 
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