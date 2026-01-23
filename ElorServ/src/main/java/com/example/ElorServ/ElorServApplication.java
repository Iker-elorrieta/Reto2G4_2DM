package com.example.ElorServ;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import Vista.Servidor;

@SpringBootApplication
public class ElorServApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElorServApplication.class, args);
		Servidor servidor = new Servidor();
		servidor.start();
	}

}
