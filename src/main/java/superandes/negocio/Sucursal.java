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
	private double tama�oInstalacion;
	
	private String clave;
	
	//---------------------------- Constructor -----------------------------//
	public Sucursal() {
		this.idSucursal = "";
		this.tama�oInstalacion = 0;
		this.clave = "";
	}

	
	public Sucursal(String idSucursal, double tamano,String pclave)
	{
		this.idSucursal = idSucursal;
		this.tama�oInstalacion = tamano;
		this.clave = pclave;
	}
	
	//--------------------------------- M�todos -------------------------------------//
	@Override
	public String getIdSucursal() {
		// TODO Auto-generated method stub
		return idSucursal;
	}

	public void setIdSucursal(String ids) {
		
		idSucursal = ids;
	}
	@Override
	public double getTama�oInstalacion() {
		// TODO Auto-generated method stub
		return tama�oInstalacion;
	}
	
	public void setTama�oInstalacion(double tam) {
		tama�oInstalacion = tam;
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
		return "Sucursal [idSucursal=" + idSucursal + ", tama�oInstalacion=" + tama�oInstalacion + ", nivelReorden=, idProveedores=" +"idSupermercado=]";
		
	}
	
}
