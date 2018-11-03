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
		long resp = (Long) q.executeUnique();
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
		Query qEstanteP = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaProductosEstante());
		Query qCarro = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaCarro());
		Query qProductoCa = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaProductosCarro());

		
		
		long bodegaEliminado = (Long) qBodega.executeUnique();
		long clienteEliminado = (Long) qCliente.executeUnique();
		long estanteEliminado = (Long) qEstante.executeUnique();
		long pedidoEliminado = (Long) qPedido.executeUnique();
		long productoEliminado = (Long) qProducto.executeUnique();
		long promocionEliminado = (Long)qPromocion.executeUnique();
		long proveedorEliminado = (Long) qProveedor.executeUnique();
		long sucursalEliminado = (Long)qSucursal.executeUnique();
		long ventaEliminado = (Long)qVenta.executeUnique();
		long p = (Long) qPro.executeUnique();
		long ventasP = (Long)qVentasp.executeUnique();
		long promocionP = (Long)qPromocionP.executeUnique();
		long bodegaP = (Long)qBodegaP.executeUnique();
		long estanteP = (Long) qEstanteP.executeUnique();
		long carro = (Long) qCarro.executeUnique();
		long carroP = (Long) qProductoCa.executeUnique();
		
		return new long[] {p, carro, carroP, ventasP, promocionP, bodegaP, estanteP,bodegaEliminado, clienteEliminado, estanteEliminado, pedidoEliminado, productoEliminado, proveedorEliminado, sucursalEliminado, promocionEliminado, ventaEliminado};
	}

}
