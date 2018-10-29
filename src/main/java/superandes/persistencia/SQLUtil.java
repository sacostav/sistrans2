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
		Query qBodega = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaBodega());Query qCliente = pm.newQuery(SQL, "DELETE FORM " + ps.darTablaCliente());
		Query qEstante = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaEstante());
		Query qPedido = pm.newQuery(SQL, "DLET FROM " + ps.darTablaPedido());
		Query qProducto = pm.newQuery(SQL, "DLEETE FROM " + ps.darTablaProducto());
		Query qProveedor = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaProveedor());
		Query qSucursal = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaSucursal());
		Query qSupermercado = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaSupermercado());
		Query qPromocion = pm.newQuery(SQL, "DELETE FROM"+ps.darTablaPromocion());
		Query qVenta = pm.newQuery(SQL, "DELETE FROM"+ps.darTablaVenta());
		Query qPro = pm.newQuery(SQL, "DELETE FROM"+ps.darTablaProductosPedidos());
		Query qprop = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaProductoProveedor());
		Query qVentasp = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaVentasProductos());
		Query qPromocionP = pm.newQuery(SQL, "DELETE FROM"+ps.darTablaPromocionProducto());
		Query qVentaC = pm.newQuery(SQL, "DELETE FROM"+ps.darTablaVentasCliente());
		Query qVentaS = pm.newQuery(SQL, "DELETE FROM"+ps.darTablaVentaSucursal());
		
		
		
		long bodegaEliminado = (long) qBodega.executeUnique();
		long clienteEliminado = (long) qCliente.executeUnique();
		long estanteEliminado = (long) qEstante.executeUnique();
		long pedidoEliminado = (long) qPedido.executeUnique();
		long productoEliminado = (long) qProducto.executeUnique();
		long proveedorEliminado = (long) qProveedor.executeUnique();
		long sucursalEliminado = (long)qSucursal.executeUnique();
		long supermercadoEliminado = (long)qSupermercado.executeUnique();
		long promocionEliminado = (long)qPromocion.executeUnique();
		long ventaEliminado = (long)qVenta.executeUnique();
		long p = (long) qPro.executeUnique();
		long suc = (long)qprop.executeUnique();
		long su = (long)qVentasp.executeUnique();
		long pi = (long)qPromocionP.executeUnique();
		long vent = (long)qVentaC.executeUnique();
		long vent1 = (long)qVentaS.executeUnique();
		
		return new long[] {p, suc, su, pi,vent,vent1,bodegaEliminado, clienteEliminado, estanteEliminado, pedidoEliminado, productoEliminado, proveedorEliminado, sucursalEliminado, supermercadoEliminado, promocionEliminado, ventaEliminado};
	}

}
