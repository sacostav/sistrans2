package superandes.negocio;

import java.util.Date;
import java.util.LinkedList;

public class Pedido implements VOPedido{


	public final static String SOLICITADO = "Solicitado";
	public final static String ENTREGADO = "Entregado";

	/* ***************************************************************
	 * Atributos
	 *****************************************************************/
	private long idPedido;

	private Date fechaPedido;

	private Date fechaLLegada;

	private String estadoPedido;
	//---------------------------- Asociaciones -----------------------------//
	private String idSucursal;

	private long idProveedor;

	//---------------------------- Constructor -----------------------------//

	public Pedido() {
		this.idPedido = 0;
		this.fechaPedido = new Date();
		this.fechaLLegada = new Date();
		this.idSucursal = "";
		this.idProveedor = 0;
		this.estadoPedido = "";
	}

	public Pedido(long id, Date fechaPedido, Date fechaLlegada , String estadoPedido,String idSucursal,long idProveedor)
	{
		this.idPedido = id;
		this.fechaPedido = fechaPedido;
		this.fechaLLegada = fechaLlegada;
		this.idSucursal = idSucursal;
		this.idProveedor = idProveedor;
		this.estadoPedido = estadoPedido;
	}

	//--------------------------------- Métodos -------------------------------------//

	
	public long getIdPedido() {
		return idPedido;
	}

	public void setIdPedido(long id)
	{
		this.idPedido = id;
	}

	
	public String getIdSucursal() {
		return idSucursal;
	}

	public void setIdSucursal(String id)
	{
		this.idSucursal = id;
	}



	
	public long getIdProveedor() {
		return idProveedor;
	}

	public void setIdProveedores(long id)
	{
		this.idProveedor = id;
	}

	
	public Date getFechaPedido() {
		return fechaPedido;
	}

	public void setFechaPedido(Date fecha)
	{
		this.fechaPedido = fecha;
	}

	
	public Date getFechaLlegada() {
		return fechaLLegada;
	}

	public void setFechaLlegada(Date fecha)
	{
		this.fechaLLegada = fecha;
	}


	
	public String getEstadoPedido() {
		return estadoPedido;
	}

	public void setEstadoPedido(String estadoPed)
	{
		this.estadoPedido = estadoPed;
	}


	
	public String toString()
	{
		return "Pedido [idPedido = " + idPedido + "fechaPedido " + fechaPedido + "fechaLlegada" + fechaLLegada + "idSucursal " + idSucursal + "idProductos idProveedor " + idProveedor + "]";
	}


}
