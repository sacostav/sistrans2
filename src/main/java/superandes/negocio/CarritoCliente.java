package superandes.negocio;

public class CarritoCliente {
	
	long idCarro, idCliente;
	
	public CarritoCliente()
	{
		idCarro = 0;
		idCliente = 0;
	}
	
	public CarritoCliente( long idCarro, long idCliente)
	{
		this.idCarro = idCarro;
		this.idCliente = idCliente;
	}
	
	public long getIdCarro()
	{
		return idCarro;
	}
	
	public void setIdCarro( long id)
	{
		this.idCarro = id;
	}
	
	public long getIdCliente()
	{
		return idCliente;
	}
	
	public void setidCliente( long id)
	{
		this.idCliente = id;
	}

}
