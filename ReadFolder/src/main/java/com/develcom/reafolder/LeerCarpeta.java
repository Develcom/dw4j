/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.reafolder;

import java.util.Enumeration;
import java.util.Properties;
import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

/**
 *
 * @author familia
 */
@Configuration
@EnableAsync
@EnableScheduling
//@SpringBootApplication
public class LeerCarpeta {

    public static void main(String[] args) {
        SpringApplication.run(LeerCarpeta.class, args);
    }

//    @RestController
//    static class HomeController {
//
//        @RequestMapping("/")
//        public String home() {
//            return "Hello World";
//        }
//    }
    
    @Scheduled(fixedRate = 5000)
    public void cargarDocumentos() {
        System.out.println("prueba agendador");
//        Properties prop = System.getProperties();
//
//        Enumeration<Object> keys = prop.keys();
//
//        while (keys.hasMoreElements()) {
//            Object nextElement = keys.nextElement();
//            System.out.println("key: " + nextElement + " - value: " + prop.get(nextElement));
//        }
    }
}
