package superandes.negocio;

public class Ventas_productos implements VOVentas_productos{
	
private long idProducto, idVenta;
	
	public Ventas_productos() {
		
		idProducto = 0;
		idVenta = 0;
	}
	
	public Ventas_productos(long idP, long idPed) {
		idProducto = idP;
		idVenta = idPed;
	}
	
	
	public long getIdProducto() {
		return idProducto;
	}
	
	
	public long getIdVenta(){
		return idVenta;
	}
	
	public void setIdProducto(long id) {
		idProducto = id;
	}
	
	public void setidVenta(long id) {
		idVenta = id;
	}

}
