package superandes.negocio;

import java.util.LinkedList;

public interface VOBodega {
	
	/* *********************************
	 * Metodos
	 ***********************************/
	
	public String getCategoria();
	
	/**
	 * @return  capacidad de la bodega 
	 */
	
	public double getVolumenBodega();
	
	public double getPesoBodega();
	
	public String getIdSucursal();
	
	@Override
	public String toString();

	public double getNivelAbastecimiento();

	public long getIdBodega();
}
