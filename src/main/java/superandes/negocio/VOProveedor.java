package superandes.negocio;

import java.util.LinkedList;
import java.util.List;
public interface VOProveedor {

	
	public int getNIT();
	
	public String getNombre();
	
	public double getCalificacionCalidad();
	
	public LinkedList<Long> getIdProductos();
	
	@Override
	
	public String toString();

	public long getIdSupermercado();
}
