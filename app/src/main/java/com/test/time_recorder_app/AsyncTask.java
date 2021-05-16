package com.test.time_recorder_app;

import android.annotation.SuppressLint;

import java.io.File;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class AsyncTask extends android.os.AsyncTask{

    private final String address;
    protected String account;
    protected String password;
    protected String title;
    protected String text;

    public AsyncTask(String address) {
        this.address = address;
    }

    @Override
    protected Object doInBackground(Object... obj){
        account=(String)obj[0];
        password=(String)obj[1];
        title=(String)obj[2];
        text=(String)obj[3];

        java.util.Properties properties = new java.util.Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        properties.put("mail.smtp.socketFactory.post", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

        javax.mail.Message message = new javax.mail.internet.MimeMessage(javax.mail.Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
            @Override
            protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                return new javax.mail.PasswordAuthentication(account,password);
            }
        }));

        try {
            message.setFrom(new javax.mail.internet.InternetAddress(account + "@gmail.com"));
//            msg.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(account + "@gmail.com")); //自分自身にメールを送信
            message.setRecipients(javax.mail.Message.RecipientType.TO, javax.mail.internet.InternetAddress.parse(address)); // 指定アドレスにメールを送信
            message.setSubject(title);
            message.setText(text);

            MimeBodyPart mimeBodyPart = new MimeBodyPart();
            @SuppressLint("SdCardPath") File attachmentFile = new File("/data/data/com.test.time_recorder_app/table1.xls");
            FileDataSource fileDataSource = new FileDataSource(attachmentFile);
            mimeBodyPart.setDataHandler(new DataHandler(fileDataSource));
            mimeBodyPart.setFileName(MimeUtility.encodeWord(fileDataSource.getName()));

            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(mimeBodyPart);

            message.setContent(multipart);

            javax.mail.Transport.send(message);

        } catch (Exception e) {
            return (Object)e.toString();
        }

        return (Object)"Sending completed";

    }
}