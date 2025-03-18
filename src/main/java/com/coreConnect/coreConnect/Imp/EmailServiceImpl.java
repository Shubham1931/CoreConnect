package com.coreConnect.coreConnect.Imp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.coreConnect.coreConnect.services.EmailService;
@Service
public class EmailServiceImpl implements EmailService {
@Autowired
private JavaMailSender emailSender;
    @Override
    public void sendEmail(String to, String subject, String body) {
        if (emailSender == null) {
            System.out.println("❌ JavaMailSender is null! Email cannot be sent.");
            return;
        }
    
       SimpleMailMessage message = new SimpleMailMessage();
       message.setTo(to);
       message.setSubject(subject);
       message.setText(body);
       message.setFrom("coreConnect@demomailtrap.co");
       emailSender.send(message);
       System.out.println("✅ Email sent successfully!");
    }

}
