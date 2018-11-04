	package superandes.negocio;

import java.util.LinkedList;

public class Venta implements VOVenta {
	
	
	//-------------------------------------------------------------------
	// Atributos 
	// -----------------------------------------------------------------
	
	private long idVenta;
	
	private double total;
	
	private long idCliente;
	
	private String idSucursal;
	
	public Venta() {
		idVenta = 0;
		total = 0;
		idCliente = 0;
		idSucursal = "";
	}
	
	public Venta(long idVenta, double total, long idCliente, String idSucursal)
	{
		this.idVenta = idVenta;
		this.total = total;
		this.idCliente = idCliente;
		this.idSucursal = idSucursal;
	}
	
	//--------------------------------------------------------------------
	// Metodos
	//--------------------------------------------------------------------
	
	public long getIdVenta() {
		return idVenta;
	}
	
	public void setIdVenta(long idVenta) {
		this.idVenta = idVenta;
	}
		
	public double getTotal() {
		return total;
	}
	
	public void setTotal(double ids) {
		this.total = ids;
	}
	
	
	public long getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(long idCliente) {
		
          this.idCliente = idCliente;
	}
	
	public String getIdSucursal()
	{
		return idSucursal;
	}
	
	public void setIdSucursal(String id){
		this.idSucursal = id;
	}
	
	
	
	

}
