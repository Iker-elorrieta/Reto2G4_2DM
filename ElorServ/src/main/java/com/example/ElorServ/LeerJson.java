package com.example.ElorServ;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import java.io.InputStreamReader;
import java.util.ArrayList;

@RestController
@RequestMapping("/centros")
public class LeerJson {
	
	private ArrayList<Centro> centros = new ArrayList<Centro>();
	
	public void obtenerJson() { 
		Gson gson = new Gson(); 
		try { 
			ClassPathResource resource = new ClassPathResource("EuskadiLatLon.json"); 
			InputStreamReader reader = new InputStreamReader(resource.getInputStream()); 
			RespuestaCentros data = gson.fromJson(reader, RespuestaCentros.class); 
			centros.addAll(data.getCENTROS()); } catch (Exception e) { e.printStackTrace();
			} 
		}
	
	@GetMapping
	public ArrayList<Centro> getCentros() {
		obtenerJson();
		return centros;
	}

}
