package superandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import oracle.net.aso.t;

public class SQLPromocion_producto {
	
	private final static String SQL = PersistenciaSuperandes.SQL;
	
	private PersistenciaSuperandes ps;
	
	public SQLPromocion_producto(PersistenciaSuperandes ps) {
		this.ps = ps;
	}
	
	public long registrarPromocionProductos(PersistenceManager pm, long idProducto, long idPromocion)
	{
		Query q = pm.newQuery(SQL, "INSERT INTO" + ps.darTablaPromocionProducto() + "(idProducto, idPromocion) values (?,?)");
		q.setParameters(idProducto, idPromocion);
		return (long) q.executeUnique();
	}
	
	public long darProductosPromocion(PersistenceManager pm, long idPromocion)
	{
		Query q = pm.newQuery(SQL, "SELECT idProducto FROM" + ps.darTablaPromocionProducto() + "WHERE idPromocion = ?");
		q.setParameters(idPromocion);
		return (long) q.executeUnique();
	}

}
