package superandes.negocio;

public class productosBodegas implements VOProductosBodegas {

	private long idBodega, idProducto;
	
	
	public productosBodegas() {
		
		idBodega = 0;
		idProducto =0;
	}
	
	public productosBodegas(long idProd, long idBodega, long id	) {
		this.idBodega = idBodega;
		this.idProducto = idProd;
	}

	
	@Override
	public long getIdProducto() {
		// TODO Auto-generated method stub
		return idProducto;
	}
	
	public void setIdProducto(long idProducto) {
		this.idProducto = idProducto;
	}

	@Override
	public long getIdBodega() {
		// TODO Auto-generated method stub
	return idBodega;
	}
	
	public void setIdBodega(long idBod) {
		idBodega = idBod;
	}
}
