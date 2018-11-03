package superandes.negocio;

public class productosEstantes implements VOProductosEstantes{

	
	long idProducto, idEstante;
	
	public productosEstantes() {
		
		idProducto = 0;
		idEstante = 0;
		
	}
	
	public productosEstantes(long idProd,long idEst) {
		idProducto = idProd;
		idEstante = idEst;
	}
	
	 
	public long getIdProducto() {
		return idProducto;
	}
	
	
	public long getIdEstante() {
		return idEstante;
	}
	
	public void setIdEstante(long id) {
		idEstante = id;
	}
	
	public void setIdProducto(long id) {
		idProducto = id;
	}
}
