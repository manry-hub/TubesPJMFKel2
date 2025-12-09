package com.tubespjmfkel2;

import javax.swing.SwingUtilities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import com.tubespjmfkel2.view.WelcomeFrame;

@SpringBootApplication
public class TubesPJMFkel2Application {

    public static void main(String[] args) {
        System.setProperty("java.awt.headless", "false");
        ConfigurableApplicationContext context = SpringApplication.run(TubesPJMFkel2Application.class, args);

        SwingUtilities.invokeLater(() -> {
            WelcomeFrame welcome = context.getBean(WelcomeFrame.class);
            welcome.setVisible(true);
        });
    }
}