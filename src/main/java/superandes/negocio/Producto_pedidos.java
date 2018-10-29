package superandes.negocio;

public class Producto_pedidos implements VOProducto_pedidos {

	private long idProducto, idPedido;
	
	public Producto_pedidos() {
		
		idProducto = 0;
		idPedido = 0;
	}
	
	public Producto_pedidos(long idP, long idPed) {
		idProducto = idP;
		idPedido = idPed;
	}
	
	@Override 
	public long getIdProducto() {
		return idProducto;
	}
	
	@Override
	public long getIdPedido(){
		return idPedido;
	}
	
	public void setIdProducto(long id) {
		idProducto = id;
	}
	
	public void setPedido(long id) {
		idPedido = id;
	}
}
