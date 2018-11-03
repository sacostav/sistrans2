package superandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import oracle.jdbc.dcn.DatabaseChangeRegistration;
import oracle.net.aso.t;

public class SQLProductos_pedidos {
	
	private static final String SQL = PersistenciaSuperandes.SQL;
	
	private PersistenciaSuperandes ps;
	
	public SQLProductos_pedidos(PersistenciaSuperandes ps) {
		this.ps = ps;
	}
	
	public long registrarProductosPedidos(PersistenceManager pm, long idProducto, long idPedido)
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + ps.darTablaProductosPedidos() + "(idProducto, idPedido) values (?,?)");
		q.setParameters(idProducto, idPedido);
		return (Long) q.executeUnique();
	}

	public long darProductosPedido(PersistenceManager pm, long idPedido)
	{
		Query q = pm.newQuery(SQL, "SELECT idProducto FROM" + ps.darTablaProductosPedidos() + "WHERE idPedido = ?");
		q.setParameters(idPedido);
		return (Long) q.executeUnique();
	}
}
