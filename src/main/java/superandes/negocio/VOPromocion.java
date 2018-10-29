package superandes.negocio;

import java.sql.Date;
import java.util.LinkedList;

public interface VOPromocion {
	
	
	public long getIdPromocion();
	
	public LinkedList<Object[]> getIdProducto();
	
	public String getTipoPromocion();
	
	@Override
	public String toString();

	public Date getFechaInicio();

	public Date getFechaFin();
	

}
