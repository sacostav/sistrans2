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
	private double tamañoInstalacion;
	
	private String clave;
	
	//---------------------------- Constructor -----------------------------//
	public Sucursal() {
		this.idSucursal = "";
		this.tamañoInstalacion = 0;
		this.clave = "";
	}

	
	public Sucursal(String idSucursal, double tamano,String pclave)
	{
		this.idSucursal = idSucursal;
		this.tamañoInstalacion = tamano;
		this.clave = pclave;
	}
	
	//--------------------------------- Métodos -------------------------------------//
	@Override
	public String getIdSucursal() {
		// TODO Auto-generated method stub
		return idSucursal;
	}

	public void setIdSucursal(String ids) {
		
		idSucursal = ids;
	}
	@Override
	public double getTamañoInstalacion() {
		// TODO Auto-generated method stub
		return tamañoInstalacion;
	}
	
	public void setTamañoInstalacion(double tam) {
		tamañoInstalacion = tam;
	}


	
	@Override
	public String getClave() {
		// TODO Auto-generated method stub
		return clave;
	}
	
	public void setClave(String clave) {
	     this.clave = clave;
	}
	@Override
	public String toString() {
		return "Sucursal [idSucursal=" + idSucursal + ", tamañoInstalacion=" + tamañoInstalacion + ", nivelReorden=, idProveedores=" +"idSupermercado=]";
		
	}
	
}
