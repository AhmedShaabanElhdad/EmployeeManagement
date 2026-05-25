package com.ahmed.employee_management_system.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    JavaMailSender mailSender;

    @Value("${backend.origin}")
    private String origin;

    @Value("${GEMAI_APP_USERNAME}")
    private String from;

    public void sendMessage(String to, String token) {
        String url = origin + "/auth/signup?token=" + token;
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject("Create Your Account");
        message.setTo(to);
        message.setText("Hi, Please create your account using this linl\n" + url);
        mailSender.send(message);
    }
}
