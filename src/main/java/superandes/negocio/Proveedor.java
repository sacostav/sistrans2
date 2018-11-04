package superandes.negocio;

import java.util.LinkedList;

public class Proveedor implements VOProveedor{
	
	
	/* *******************************************
	 * Atributos
	 *********************************************/

	
	/**
	 * NIT del proveedor
	 */
	private int NIT;
	
	/**
	 * Nombre del proveedor
	 */
	private String nombre;
	
	/**
	 * Calificación calidad del provedor
	 */
	
	private double calificacionCalidad;
	
	
	//---------------------------- Constructor -----------------------------//

	public Proveedor() {
		this.NIT = 0;
		this.nombre ="";
		this.calificacionCalidad = 0;
	}
	
	public Proveedor(int nit, String nombre, double calificion)
	{
		this.NIT = nit;
		this.nombre = nombre;
		this.calificacionCalidad = calificion;
	}
	
	//--------------------------------- Métodos -------------------------------------//
	
	public int getNIT() {
		// TODO Auto-generated method stub
		return NIT;
	}
	
	public void setNIT(int Nit) {
		NIT = Nit;
	}

	
	public String getNombre() {
		// TODO Auto-generated method stub
		return nombre;
	}
	
	public void setNombre(String nom) {
		nombre = nom;
	}

	
	public double getCalificacionCalidad() {
		// TODO Auto-generated method stub
		return calificacionCalidad;
	}
	
	public void setCalificacionCalidad(double calf) {
		calificacionCalidad = calf;
	}

	
	
	public String toString() {
		return "Proveedor [NIT=" + NIT + ", nombre=" + nombre + ", calificacionCalidad=" + calificacionCalidad + "]";

	}

	
	public LinkedList<Long> getIdProductos() {
		// TODO Auto-generated method stub
		return null;
	}

	
	public long getIdSupermercado() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
}
