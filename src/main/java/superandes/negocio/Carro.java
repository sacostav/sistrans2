package superandes.negocio;

public class Carro {

	long idCarro;

	String idSucursal;

	public Carro()
	{
		idCarro = 0;
		idSucursal = "";
	}
	
	public Carro(long idCarro, String idSucursal)
	{
		this.idCarro = idCarro;
		this.idSucursal = idSucursal;
	}
	
	public long getIdCarro()
	{
		return idCarro;
	}
	
	public void setIdcarro( long id)
	{
		this.idCarro = id;
	}
	
	public String getIdSucursal()
	{
		return idSucursal;
	}
	
	public void setIdSucursal( String id)
	{
		this.idSucursal = id;
	}

}
