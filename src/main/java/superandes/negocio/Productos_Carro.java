package superandes.negocio;

public class Productos_Carro {
	
	long idCarro;
	long idProducto;
	
	public Productos_Carro()
	{
		idCarro = 0;
		idProducto = 0;
	}
	
	public Productos_Carro(long idCarro, long idProducto)
	{
		this.idCarro = idCarro;
		this.idProducto = idProducto;
	}
	
	public long getIdCarro()
	{
		return idCarro;
	}
	
	public void setIdCarro( long idCarro)
	{
		this.idCarro = idCarro;
	}
	
	public long getIdProducto()
	{
		return idProducto;
	}
	
	public void setIdProducto(long idProducto)
	{
		this.idProducto = idProducto;
	}

}
