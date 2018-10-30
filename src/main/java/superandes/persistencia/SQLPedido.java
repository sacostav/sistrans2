package superandes.persistencia;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import oracle.net.aso.t;
import superandes.negocio.Estante;
import superandes.negocio.Pedido;

public class SQLPedido {

	private final static String SQL = PersistenciaSuperandes.SQL;

	private PersistenciaSuperandes ps;

	public SQLPedido(PersistenciaSuperandes ps )
	{
		this.ps = ps;
	}

	

	public long registrarPedido(PersistenceManager pm, long idPedido, Date fechaPedido, Date fechaLlegada, String idSucursal, long idProveedores)
	{
		
		Query q = pm.newQuery(SQL, "INSERT INTO " + ps.darTablaPedido() + "(id, fechaPedido ,fechaLlegada, estadoPedido, idSucursal, idProveedor, estado) values (?,?,?,?,?,?)");
		q.setParameters(idPedido, fechaPedido, fechaLlegada,  Pedido.SOLICITADO, idSucursal, idProveedores);
		return (long) q.executeUnique();
	}

	
	public long registrarLlegadaPedido( PersistenceManager pm, Date pFechaLlegada, long idPedido)
	{

		Query q = pm.newQuery(SQL, "UPDATE " + ps.darTablaPedido() + "SET fechaLlegada = ?, estadoPedido ="+Pedido.ENTREGADO+ "WHERE idPedido = ?");
		q.setParameters(pFechaLlegada, idPedido);
		return (long) q.executeUnique();
	}
	
	public List<Pedido> darPedidos(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + ps.darTablaEstante());
		q.setResultClass(Pedido.class);
		return q.executeResultList();
	}

}

