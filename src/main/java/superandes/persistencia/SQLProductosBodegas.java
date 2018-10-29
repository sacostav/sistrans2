package superandes.persistencia;

import java.util.Date;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superandes.negocio.Producto;

public class SQLProductosBodegas {

	private static final String SQL = PersistenciaSuperandes.SQL;

	private PersistenciaSuperandes ps;
	
	public SQLProductosBodegas(PersistenciaSuperandes ps) {
		this.ps = ps;
	}
	
	public long registrarProductoEnBodega(PersistenceManager pm, long idProducto, long idBodega)
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + ps.darTablaProductosBodega() + "(idProducto, idBodega) values (?,?)");
		q.setParameters(idProducto,idBodega);
		return (long) q.executeUnique();
	}
	
	public long eliminarProductoEnBodegaPorId (PersistenceManager pm, long idProducto)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaProductosBodega() + " WHERE idProducto = ?");
        q.setParameters(idProducto);
        return (long) q.executeUnique();            
	}
	
	public int cantidadProductoEnBodega(PersistenceManager pm, String codigoBarras) {
		Query q = pm.newQuery(SQL, "SELECT COUNT (*) FROM "+ps.darTablaProductosBodega()+" INNER JOIN ON "+ps.darTablaProducto()+" ON "+ps.darTablaProductosBodega()+".idProducto="+ps.darTablaProducto()+"idProducto WHERE "+ps.darTablaProducto()+".codigoBarras = ?");
		q.setParameters(codigoBarras);
		return (int) q.executeUnique();
	}
	
	
	
}
