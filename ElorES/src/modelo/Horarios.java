package modelo;

import java.sql.Timestamp;


public class Horarios {

	
	private Integer id;
	private Modulos modulos;
	private String dia;
	private byte hora;
	private String aula;
	private String observaciones;
	private Timestamp createdAt;
	private Timestamp updatedAt;
	
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Modulos getModulos() {
		return modulos;
	}
	public void setModulos(Modulos modulos) {
		this.modulos = modulos;
	}
	public String getDia() {
		return dia;
	}
	public void setDia(String dia) {
		this.dia = dia;
	}
	public byte getHora() {
		return hora;
	}
	public void setHora(byte hora) {
		this.hora = hora;
	}
	public String getAula() {
		return aula;
	}
	public void setAula(String aula) {
		this.aula = aula;
	}
	public String getObservaciones() {
		return observaciones;
	}
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}
	public Timestamp getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Timestamp createdAt) {
		this.createdAt = createdAt;
	}
	public Timestamp getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(Timestamp updatedAt) {
		this.updatedAt = updatedAt;
	}
	
	

}
