package superandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLVentas_productos {
private final static String SQL = PersistenciaSuperandes.SQL;
	
	private PersistenciaSuperandes ps;
	
	public SQLVentas_productos(PersistenciaSuperandes ps) {
		this.ps = ps;
	}
	
	public long registrarVentasProductos(PersistenceManager pm, long idVenta, long idProducto)
	{
		Query q = pm.newQuery(SQL, "INSERT INTO" + ps.darTablaVentasProductos() + "(idVenta, idProducto) values (?,?)");
		q.setParameters(idVenta, idProducto);
		return (Long) q.executeUnique();
	}
	
	public long darProductosVenta(PersistenceManager pm, long idVenta)
	{
		Query q = pm.newQuery(SQL, "SELECT idProducto FROM" + ps.darTablaVentasProductos() + "WHERE idVenta = ?");
		q.setParameters(idVenta);
		return (Long) q.executeUnique();
	}


}
