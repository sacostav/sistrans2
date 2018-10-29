	package superandes.negocio;

import java.util.LinkedList;

public class Venta implements VOVenta {
	
	
	//-------------------------------------------------------------------
	// Atributos 
	// -----------------------------------------------------------------
	
	private long idVenta;
	
	private double total;
	
	private long idCliente;
	
	
	public Venta() {
		idVenta = 0;
		total = 0;
		idCliente = 0;
	}
	
	public Venta(long idVenta, double total, long idCliente)
	{
		this.idVenta = idVenta;
		this.total = total;
		this.idCliente = idCliente;
	}
	
	//--------------------------------------------------------------------
	// Metodos
	//--------------------------------------------------------------------
	
	@Override
	public long getIdVenta() {
		return idVenta;
	}
	
	public void setIdVenta(long idVenta) {
		this.idVenta = idVenta;
	}
		
	@Override
	public double getTotal() {
		return total;
	}
	
	public void setTotal(double ids) {
		this.total = ids;
	}
	
	
	@Override
	public long getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(long idCliente) {
		
          this.idCliente = idCliente;
	}
	
	
	
	

}
