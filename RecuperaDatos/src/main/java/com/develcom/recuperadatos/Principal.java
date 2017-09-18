/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.develcom.recuperadatos;

import com.develcom.recupera.RecuperaBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@SpringBootApplication
public class Principal {

    public static void main(String[] args) {
        SpringApplication.run(Principal.class, args);
        AnnotationConfigApplicationContext contexto = new AnnotationConfigApplicationContext();
        contexto.register(AppConfig.class);
        contexto.refresh();
        
        RecuperaBean recuperaBean = contexto.getBean(RecuperaBean.class);
        
        recuperaBean.restaurarDatos();
    }

}
