package de.unileipzig.bis.rbs.testApp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Simple spring boot starter app
 *
 * @author Lukas Werner
 */
@SpringBootApplication
public class TestApp {

    /**
     * Execute the application
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(TestApp.class, args);
    }

}