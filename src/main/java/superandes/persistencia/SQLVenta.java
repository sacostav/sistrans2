package superandes.persistencia;

import java.util.LinkedList;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superandes.negocio.Bodega;
import superandes.negocio.Venta;

import java.util.List;
public class SQLVenta {

	

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaSuperandes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaSuperandes pp;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLVenta (PersistenciaSuperandes pp)
	{
		this.pp = pp;
	}

	
	/**
	 * Adiciona una nueva venta a la base de datos 
	 * @param pm - manejador de la persistencia
	 * @param idVenta - identificador de la venta
	 * @param idProducto - identificadores de los productos
	 * @param idCliente - id del cliente
	 * @return numero de tuplas insertadas 
	 */

	public long adicionarVenta (PersistenceManager pm, long idVenta, List<Long> idProducto, long idCliente, String idSucursal) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaVenta() + "(idVenta, idProducto, idCliente, idSucursal) values (?, ?, ?)");
		q.setParameters(idVenta, idProducto, idCliente, idSucursal);
		return (long) q.executeUnique();
	}
	
	
	/**
	 * Retorna una venta con el mismo id recibido por parametro 
	 * @param pm manejador de persistencia
	 * @param idVenta identificador de la venta
	 * @return venta 
	 */
	
	public Venta darVentaPorId (PersistenceManager pm, String idVenta) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaVenta () + " WHERE idBodega = ?");
		q.setResultClass(Venta.class);
		q.setParameters(idVenta);
		return (Venta) q.executeUnique();
	}

	/**
	 * Retorna una lista con todas las ventas 
	 * @param pm manejador de la persistencia 
	 * @return
	 */
	public List<Venta> darVentas (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaVenta ());
		q.setResultClass(Venta.class);
		return (List<Venta>) q.executeList();
	}
	
	public List<Venta> darVentasIdSucursal (PersistenceManager pm, String idSucursal){
		
		Query q = pm.newQuery(SQL, "SELECT * FROM "+pp.darTablaVenta()+ " WHERE idSucursal = ?");
		q.setResultClass(Venta.class);
		q.setParameters(idSucursal);
		return (List<Venta>) q.executeList();
		
	}
	
	/*
	public double totalCosto(PersistenceManager pm, long idVenta) {
		Query query = pm.newQuery(SQL, "SELECT idProducto FROM "+pp.darTablaVenta()+" WHERE idVenta = ?");
		query.setParameters(idVenta);
		List<Long> idProductos =(List<Long>) query.executeList();
		
		while(idProductos.iterator().hasNext()) {

			Long id 
		}
		
	}*/
}
