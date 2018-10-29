package superandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import oracle.net.aso.t;

public class SQLUtil {
	
	private final static String SQL = PersistenciaSuperandes.SQL;
	
	private PersistenciaSuperandes ps;
	
	
	public SQLUtil(PersistenciaSuperandes ps)
	{
		this.ps = ps;
	}
	
	public long nextval(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT " + ps.darSeqSuperandes() + ".nextval FROM DUAL");
		q.setResultClass(Long.class);
		long resp = (long) q.executeUnique();
		return resp;
	}
	
	public long[] limpiarSuperandes(PersistenceManager pm)
	{
		Query qBodega = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaBodega());
		Query qCliente = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaCliente());
		Query qEstante = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaEstante());
		Query qPedido = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaPedido());
		Query qProducto = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaProducto());
		Query qPromocion = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaPromocion());
		Query qProveedor = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaProveedor());
		Query qSucursal = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaSucursal());
		Query qVenta = pm.newQuery(SQL, "DELETE FROM"+ps.darTablaVenta());
		Query qPro = pm.newQuery(SQL, "DELETE FROM"+ps.darTablaProductosPedidos());
		Query qVentasp = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaVentasProductos());
		Query qPromocionP = pm.newQuery(SQL, "DELETE FROM"+ps.darTablaPromocionProducto());
		Query qBodegaP = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaProductosBodega());
		Query qEstanteP = pm.newQuery(SQL, "DLEETE FROM " + ps.darTablaProductosEstante());

		
		
		long bodegaEliminado = (long) qBodega.executeUnique();
		long clienteEliminado = (long) qCliente.executeUnique();
		long estanteEliminado = (long) qEstante.executeUnique();
		long pedidoEliminado = (long) qPedido.executeUnique();
		long productoEliminado = (long) qProducto.executeUnique();
		long promocionEliminado = (long)qPromocion.executeUnique();
		long proveedorEliminado = (long) qProveedor.executeUnique();
		long sucursalEliminado = (long)qSucursal.executeUnique();
		long ventaEliminado = (long)qVenta.executeUnique();
		long p = (long) qPro.executeUnique();
		long ventasP = (long)qVentasp.executeUnique();
		long promocionP = (long)qPromocionP.executeUnique();
		long bodegaP = (long)qBodegaP.executeUnique();
		long estanteP = (long) qEstanteP.executeUnique();
		
		return new long[] {p, ventasP, promocionP, bodegaP, estanteP,bodegaEliminado, clienteEliminado, estanteEliminado, pedidoEliminado, productoEliminado, proveedorEliminado, sucursalEliminado, promocionEliminado, ventaEliminado};
	}

}
