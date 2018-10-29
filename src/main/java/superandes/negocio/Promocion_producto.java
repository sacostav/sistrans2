package superandes.negocio;

public class Promocion_producto implements VOPromocion_producto{

private long idProducto, idPromocion;
	
	public Promocion_producto() {
		
		idProducto = 0;
		idPromocion = 0;
	}
	
	public Promocion_producto(long idP, long idProm) {
		idProducto = idP;
		idPromocion = idProm;
	}
	
	@Override 
	public long getIdProducto() {
		return idProducto;
	}
	
	@Override
	public long getIdPromocion(){
		return idPromocion;
	}
	
	public void setIdProducto(long id) {
		idProducto = id;
	}
	
	public void setPedido(long id) {
		idPromocion = id;
	}
}
