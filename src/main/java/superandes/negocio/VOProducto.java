package superandes.negocio;

import java.util.Date;

public interface VOProducto {
	
	/* **************************************
	 * Metodos
	 ****************************************/
	public long getIdProducto();
	
	public String getNombre();
	
	public String getMarca();
	
	public double getPrecioUnitario();
	
	public double getPrecioUnidadMedida();
	
	public double getVolumenEmpaque();
	
	public double getPesoEmpaque();
	
	public String getCodigoBarras();
	
	public Date getFechaVencimiento();
	
	public int getNivelReorden();

	public double getPrecioProveedor();
}
