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
	 * tama�o instalacion
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
	
	//--------------------------------- M�todos -------------------------------------//
	
	public String getIdSucursal() {
		// TODO Auto-generated method stub
		return idSucursal;
	}

	public void setIdSucursal(String ids) {
		
		idSucursal = ids;
	}
	
	public double getTama�oInstalacion() {
		// TODO Auto-generated method stub
		return tamanioInstalacion;
	}
	
	public void setTama�oInstalacion(double tam) {
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
		return "Sucursal [idSucursal=" + idSucursal + ", tama�oInstalacion=" + tamanioInstalacion + ", nivelReorden=, idProveedores=" +"idSupermercado=]";
		
	}
	
}
