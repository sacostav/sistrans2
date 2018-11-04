package superandes.negocio;

import java.util.LinkedList;

public class Sucursal implements VOSucursal{

	/* *********************************************
	 * Atributos
	 ***********************************************/
	
	/**
	 * id de la sucursal
	 */
	
	private String idSucursal;
	
	/**
	 * tamaño instalacion
	 */
	private double tamanioInstalacion;
	
	private String clave;
	
	//---------------------------- Constructor -----------------------------//
	public Sucursal() {
		this.idSucursal = "";
		this.tamanioInstalacion = 0;
		this.clave = "";
	}

	
	public Sucursal(String idSucursal, double tamano,String pclave)
	{
		this.idSucursal = idSucursal;
		this.tamanioInstalacion = tamano;
		this.clave = pclave;
	}
	
	//--------------------------------- Métodos -------------------------------------//
	
	public String getIdSucursal() {
		// TODO Auto-generated method stub
		return idSucursal;
	}

	public void setIdSucursal(String ids) {
		
		idSucursal = ids;
	}
	
	public double getTamañoInstalacion() {
		// TODO Auto-generated method stub
		return tamanioInstalacion;
	}
	
	public void setTamañoInstalacion(double tam) {
		tamanioInstalacion = tam;
	}


	
	
	public String getClave() {
		// TODO Auto-generated method stub
		return clave;
	}
	
	public void setClave(String clave) {
	     this.clave = clave;
	}
	
	public String toString() {
		return "Sucursal [idSucursal=" + idSucursal + ", tamañoInstalacion=" + tamanioInstalacion + ", nivelReorden=, idProveedores=" +"idSupermercado=]";
		
	}
	
}
