package superandes.negocio;

import java.util.LinkedList;

public interface VOSucursal {

	public String getIdSucursal();
	
	public double getTamañoInstalacion();
		
	@Override
	public String toString();

	public String getClave();
}
