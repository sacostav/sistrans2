package superandes.negocio;

import java.util.Date;
import java.util.LinkedList;

public interface VOPromocion {
	
	
	public long getIdPromocion();
		
	public String getTipoPromocion();
	
	@Override
	public String toString();

	public Date getFechaInicio();

	public Date getFechaFin();
	

}
