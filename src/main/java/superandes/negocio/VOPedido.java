package superandes.negocio;

import java.util.Date;
import java.util.LinkedList;

public interface VOPedido {
	
	public long getIdPedido();
	
	public Date getFechaPedido();
	
	public Date getFechaLlegada();
	
	public String getIdSucursal();
	
	public long getIdProveedor();

	public String getEstadoPedido();

}
