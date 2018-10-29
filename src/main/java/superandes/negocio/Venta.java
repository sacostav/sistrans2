	package superandes.negocio;

import java.util.LinkedList;

public class Venta implements VOVenta {
	
	
	//-------------------------------------------------------------------
	// Atributos 
	// -----------------------------------------------------------------
	
	private long idVenta;
	
	private double total;
	
	private LinkedList<Object[]> ventaProductos;
	
	private long idCliente;
	
	private String idSucursal;
	
	
	public Venta() {
		idVenta = 0;
		total = 0;
		ventaProductos = new LinkedList<Object[]>();
		idCliente = 0;
		idSucursal ="";
	}
	
	public Venta(long idVenta, double total, long idCliente, String idSucursal)
	{
		this.idVenta = idVenta;
		this.total = total;
		this.idCliente = idCliente;
		this.idSucursal = idSucursal;
		ventaProductos = new LinkedList<Object[]>();
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
	public String getIdSucursal() {
		return idSucursal;
	}
	
	public void setIdSucursal(String ids) {
		this.idSucursal = ids;
	}
	
	@Override
	public double getTotal() {
		return total;
	}
	
	public void setTotal(double ids) {
		this.total = ids;
	}
	
	@Override
	public LinkedList<Object[]> getIdProductos(){
		return ventaProductos;
	}
	
	public void setGetIdProductos(LinkedList<Object[]> list) {
		
		ventaProductos = list;
	}
	
	@Override
	public long getIdCliente() {
		return idCliente;
	}
	
	public void setIdCliente(long idCliente) {
		
          this.idCliente = idCliente;
	}
	
	
	
	

}
