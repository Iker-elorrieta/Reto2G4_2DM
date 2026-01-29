package com.example.ElorServ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import Vista.Servidor;

@SpringBootApplication
@ComponentScan(basePackages = {
    "com.example.ElorServ",
    "Controlador",
    "Vista",
    "modelo"
})

public class ElorServApplication {

    public static void main(String[] args) {
        var context = SpringApplication.run(ElorServApplication.class, args);

        Servidor servidor = context.getBean(Servidor.class);
        servidor.start();
    }
}
