package com.kitchenFiller;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class SendEmail {

    private String email;

    public SendEmail(String email){
        this.email = email;
    }

    public void changeEmail(String newEmail){
        this.email = newEmail;
    }

    public String getEmail(){
        return this.email;
    }

    public void sendRealEmail(Item product) {
        // Recipient's email ID needs to be mentioned.
        String to = this.getEmail();

        // Sender's email ID needs to be mentioned
        String from = "web@gmail.com";

        // Assuming you are sending email from localhost
        String host = "localhost";

        // Get system properties
        Properties properties = System.getProperties();

        // Setup mail server
        properties.setProperty("mail.smtp.host", host);

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties);

        try {
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

            // Set Subject: header field
            message.setSubject("You are running low on " + product.getName().toLowerCase());

            // Now set the actual message
            message.setText("You can order from the following websites: \n" + "1 - " + this.getLinkForArons(product.getName()) + "\n" + "2 - " + this.getLinkForWalmart(product.getName())  + "\n" + "3 - " + this.getLinkForSeasons(product.getName())  + "\n" + "4 - " + this.getLinkForWasserman(product.getName()));

            // Send message
            Transport.send(message);
            //System.out.println("Sent message successfully....");
            
        } catch (MessagingException mex) {
            mex.printStackTrace();
        }
    }

    public String getLinkForArons(String product){
        String link = "https://www.aronskissenafarms.com/search/";
        product = product.replaceAll(" ", "%20");
        link = link + product;
        return link;
    }

    private String getLinkForSeasons(String product){
        String link = "https://www.seasonskosher.com/queens/#!?q=";
        product = product.replaceAll(" ", "%20");
        link = link + product;
        return link;
    }

    private String getLinkForWalmart(String product){
        String link = "https://www.walmart.com/search?q=";
        product = product.replaceAll(" ", "+");
        link = link + product;
        return link;
    }

    private String getLinkForWasserman(String product){
        String link = "https://www.wassermansupermarket.com/#!flushing/?q=";
        product = product.replaceAll(" ", "%20");
        link = link + product;
        return link;
    }
}
