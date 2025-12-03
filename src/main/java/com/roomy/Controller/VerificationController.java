package com.roomy.Controller;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class VerificationController {

    //Méthode publique et statique pour envoyer un OTP pour le mdp
    public static void sendOTPmdp(String toEmail, String otpCode){
        // Adresse Gmail et mot de passe d'application
        final String fromEmail = "chaouchzineb26@gmail.com";
        final String password = "ymugjvdvbibwjkor";

        // Configuration SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Création de la session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Votre code de vérification pour reinisialiser le mot de passe est : ");
            message.setText("Code : " + otpCode);

            // Envoi du mail
            Transport.send(message);
            System.out.println("ecrire le code envoyé à " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
    // Méthode publique et statique pour envoyer un OTP
    public static void sendOtpEmail(String toEmail, String otpCode) {
        // Adresse Gmail et mot de passe d'application
        final String fromEmail = "chaouchzineb26@gmail.com";
        final String password = "ymugjvdvbibwjkor";

        // Configuration SMTP
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Création de la session
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Votre code de vérification");
            message.setText("Votre code OTP est : " + otpCode);

            // Envoi du mail
            Transport.send(message);
            System.out.println("OTP envoyé à " + toEmail);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
