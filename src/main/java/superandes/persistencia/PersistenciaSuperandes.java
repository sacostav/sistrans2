package superandes.persistencia;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import superandes.negocio.Sucursal;
import superandes.negocio.Ventas_productos;
import superandes.negocio.Bodega;
import superandes.negocio.Cliente;
import superandes.negocio.Estante;
import superandes.negocio.Pedido;
import superandes.negocio.Producto;
import superandes.negocio.Producto_pedidos;
import superandes.negocio.Promocion;
import superandes.negocio.Promocion_producto;
import superandes.negocio.Proveedor;

import java.sql.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import javax.jdo.annotations.Cacheable;

public class PersistenciaSuperandes {

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PersistenciaSuperandes.class.getName());

	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	private static PersistenciaSuperandes instance;

	private PersistenceManagerFactory pmf;

	private LinkedList<String> tablas;

	private SQLUtil sqlUtil;

	private SQLBodega sqlBodega;

	private SQLCliente sqlCliente;

	private SQLEstante sqlEstante;

	private SQLPedido sqlPedido;

	private SQLProducto sqlProducto;

	private SQLProveedor sqlProveedor;

	private SQLSucursal sqlSucursal;

	private SQLPromocion sqlPromocion;
	private SQLProductos_pedidos sqlProductos_pedidos;

	private SQLVentas_productos sqlVentas_productos;

	private SQLPromocion_producto sqlPromocion_producto;
	
	private SQLProductosBodegas sqlProductosBodega;
	
	private SQLProductosEstantes sqlProductosEstante;



	/* ****************************************************************
	 * 			Métodos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - Patrón SINGLETON
	 */

	public PersistenciaSuperandes( ) {

		pmf = JDOHelper.getPersistenceManagerFactory("Superandes");
		crearClasesSQL();

		tablas = new LinkedList<String>();
		tablas.add("Superandes_sequence");
		tablas.add("BODEGA");
		tablas.add("CLIENTE");
		tablas.add("ESTANTE");
		tablas.add("PEDIDO");
		tablas.add("PRODUCTO");
		tablas.add("PROVEEDOR");
		tablas.add("SUCURSAL");
		tablas.add("SUPERMERCADO");
		tablas.add("PROMOCION");
		tablas.add("VENTA");
		tablas.add("PRODUCTOS_PEDIDOS");
		tablas.add("VENTAS_PRODUCTOS");
		tablas.add("PROMOCION_PRODUCTO");
		tablas.add("PRODUCTOS_BODEGA");
		tablas.add("PRODUCTOS_ESTANTE");
		
	}

	private PersistenciaSuperandes(JsonObject tableConfig)
	{
		crearClasesSQL();
		tablas = leerNombreTablas(tableConfig);


		String unidadPersistencia = tableConfig.get ("unidadPersistencia").getAsString ();
		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory (unidadPersistencia);

	}


	public static  PersistenciaSuperandes getInstance() {

		if( instance == null)
		{
			instance = new PersistenciaSuperandes();
		}
		return instance;
	}

	public static PersistenciaSuperandes getInstance(JsonObject tableConfig) {

		if(instance == null)
		{
			instance = new PersistenciaSuperandes(tableConfig);
		}
		return instance;
	}

	public void cerrarUnidadPersistencia() {
		pmf.close();
		instance = null;
	}

	private LinkedList<String> leerNombreTablas(JsonObject tableConfig) {

		JsonArray nombres = tableConfig.getAsJsonArray("tablas");

		LinkedList<String> resp = new LinkedList<String>();
		for(JsonElement nom : nombres)
		{
			resp.add(nom.getAsString());
		}
		return resp;
	}

	private void crearClasesSQL()
	{
		sqlBodega = new SQLBodega(this);
		sqlCliente = new SQLCliente(this);
		sqlEstante = new SQLEstante(this);
		sqlPedido = new SQLPedido(this);
		sqlProducto = new SQLProducto(this);
		sqlProveedor = new SQLProveedor(this);
		sqlSucursal = new SQLSucursal(this);
		sqlPromocion = new SQLPromocion(this);
		sqlVenta = new SQLVenta(this);
		sqlUtil = new SQLUtil(this);
		sqlProductosBodega = new SQLProductosBodegas(this);

	}

	public String darSeqSuperandes()
	{
		return tablas.get(0);
	}

	public String darTablaBodega() {
		return tablas.get(1);
	}

	public String darTablaCliente() {
		return tablas.get(2);
	}

	public String darTablaEstante() {
		return tablas.get(3);
	}

	public String darTablaPedido(){
		return tablas.get(4);
	}

	public String darTablaProducto() {
		return tablas.get(5);
	}

	public String darTablaProveedor() {
		return tablas.get(6);
	}

	public String darTablaSucursal() {
		return tablas.get(7);
	}

	public String darTablaSupermercado() {
		return tablas.get(8);
	}


	public String darTablaPromocion() {
		return tablas. get(9);
	}


	public String darTablaVenta() {
		return tablas.get(10);
	}


	public String darTablaProductosPedidos() {
		return tablas.get(11);
	}



	public String darTablaVentasProductos() {
		return tablas.get(12);
	}

	public String darTablaPromocionProducto() {
		return tablas.get(13);
	}

	public String darTablaProductosBodega() {
		return tablas.get(14);
	}

	public String darTablaProductosEstante() {
		return tablas.get(15);
	}
	private String darDetalleException(Exception e)
	{
		String resp = "";
		if(e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions()[0].getMessage();
		}
		return resp;	
	}

	private long nextval()
	{
		long resp = sqlUtil.nextval(pmf.getPersistenceManager());
		log.trace("Generando secuencia: " + resp);
		return resp;
	}

	/* ****************************************************************
	 * 			Métodos para manejar la BODEGA
	 *****************************************************************/

	//TODO RF5

	public Bodega adicionarBodega(String categoria, double peso, double volumen, String idSucursal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{   long idBodega = nextval();
		tx.begin();
		long tuplasInsertadas = sqlBodega.adicionarBodega(pm, idBodega, peso, volumen, categoria, idSucursal);
		tx.commit();

		log.trace ("Inserción de bodega: " + categoria + ": " + tuplasInsertadas + " tuplas insertadas");

		return new Bodega(idBodega,peso, volumen, categoria,idSucursal);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public Bodega darBodegaPorId(long idBodega) {
		return sqlBodega.darBodegaPorId (pmf.getPersistenceManager(), idBodega);
	}

	public int cantProductosEnBodega(long idBodega) {
		return sqlBodega.cantProductosEnBodega(pmf.getPersistenceManager(), idBodega);
	}

	/* ****************************************************************
	 * 			Métodos para manejar los CLIENTES
	 *****************************************************************/


	//RF3

	public Cliente adicionarCliente(int documento, String nombre, int NIT, String correo, String direccion,String tipo, long idSup)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idCliente = nextval ();
			long tuplasInsertadas = sqlCliente.registrarCliente(pm, idCliente,documento, NIT,  nombre,correo, direccion, tipo, idSup);
			tx.commit();

			log.trace ("Inserción de cliente: " + nombre + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Cliente (idCliente,documento, NIT, nombre, correo, direccion, tipo, idSup);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}


	}

	public Cliente darClienteId(long id){
		return sqlCliente.darClienteId(pmf.getPersistenceManager(), id);
	}



	/* ****************************************************************
	 * 			Métodos para manejar los ESTANTES
	 *****************************************************************/

	// RF6 
	public Estante adicionarEstante(int nivelAbastecimiento , double peso, double volumen, String categoria, String idSucursal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			long idEstante = nextval();
			tx.begin();
			long tuplasInsertadas = sqlEstante.adicionarEstante(pm,idEstante, nivelAbastecimiento,volumen, peso, categoria, idSucursal);
			tx.commit();

			log.trace ("Inserción del estante: " + categoria+ "-"+idSucursal+ ": " + tuplasInsertadas + " tuplas insertadas");

			return new Estante(idEstante, nivelAbastecimiento,volumen, peso, categoria, idSucursal);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}


	public Estante darEstantePorSucursalyCategoria(String idSucursal, String categoria) {
		return sqlEstante.darEstantePorCategoriaySucursal(pmf.getPersistenceManager(), categoria, idSucursal);
	}
	/* ****************************************************************
	 * 			Métodos para manejar los PEDIDOS
	 *****************************************************************/

	// RF9
	public Pedido adicionarPedido(java.util.Date fechaPedido, java.util.Date fechaLlegada ,String idSucursal,long idProveedor, String estadoPedido)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idPedido = nextval ();
			long tuplasInsertadas = sqlPedido.registrarPedido(pm, idPedido, fechaPedido, fechaLlegada, idSucursal, idProveedor);
			tx.commit();

			log.trace ("Inserción del pedido: " +idPedido+ ": " + tuplasInsertadas + " tuplas insertadas");

			return new Pedido (idPedido, fechaPedido, fechaLlegada, Pedido.ENTREGADO, idSucursal, idProveedor);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long registrarLlegadaPedido(long idPedido, java.util.Date fechaLlegada) {

		return sqlPedido.registrarLlegadaPedido(pmf.getPersistenceManager(), fechaLlegada, idPedido);

	}

	/* ****************************************************************
	 * 			Métodos para manejar los PRODUCTOS
	 *****************************************************************/


	// RF2
	public Producto adicionarProducto(String nombre, String marca, double precioUnitario, double precioUnidadMedida, String unidadMed, double volumenEmpaque, double pesoEmpaque, String codigoBarras, Date fechaVencimiento, int nivelReorden, double precioProveedor)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idProducto = nextval ();
			long tuplasInsertadas = sqlProducto.registrarProducto(pm, idProducto, nombre, marca, precioUnitario, precioUnidadMedida, unidadMed, volumenEmpaque, pesoEmpaque, codigoBarras, fechaVencimiento, nivelReorden, precioProveedor);
			tx.commit();

			log.trace ("Inserción del producto: " +idProducto+ ": " + tuplasInsertadas + " tuplas insertadas");

			return new Producto (idProducto, nombre, marca, precioUnitario, precioUnidadMedida, unidadMed, volumenEmpaque, pesoEmpaque, codigoBarras, fechaVencimiento, nivelReorden, precioProveedor);
		}
		catch (Exception e)
		{
			//        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public Producto darProductoId(long id) {
		return sqlProducto.darProductoId(pmf.getPersistenceManager(), id);
	}


	/* ****************************************************************
	 * 			Métodos para manejar los PROMOCION
	 *****************************************************************/


	// RF7
	public Promocion adicionarPromocion(String tipoPromocion,Date fechaFin, Date fechaInicio)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idPromocion = nextval ();
			long tuplasInsertadas = sqlPromocion.registrarPromocion(pm, idPromocion, tipoPromocion, fechaFin, fechaInicio);
			tx.commit();

			log.trace ("Inserción del producto: " +idPromocion + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Promocion(idPromocion, tipoPromocion, fechaFin, fechaInicio);
		}
		catch (Exception e)
		{
			//	        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public Promocion darPromocionId(long id) {
		return sqlPromocion.darPromocionPorId(pmf.getPersistenceManager(), id);
	}

	/* ****************************************************************
	 * 			Métodos para manejar los PROVEEDOR
	 *****************************************************************/
	public Proveedor adicionarProveedor(int nit,String nombre, int calificacion, LinkedList<Long> idProductos, long idSupermercado)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long tuplasInsertadas = sqlProveedor.adicionarProveedor(pm, nit, nombre, calificacion, idProductos, idSupermercado);
			tx.commit();

			log.trace ("Inserción del producto: " +nit + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Proveedor(nit, nombre, calificacion, idProductos, idSupermercado);
		}
		catch (Exception e)
		{
			//	        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public Proveedor darProveedorNit(int nit) {

		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			Proveedor resp = sqlProveedor.darProveedorPorNit(pm, nit);
			tx.commit();

			return resp;
		}
		catch (Exception e)
		{
			//	        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<Proveedor> darProveedores()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			List<Proveedor> resp = sqlProveedor.darSucursales(pm);
			tx.commit();

			return resp;
		}
		catch (Exception e)
		{
			//	        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<Long> darIdProductos(int nit)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			List<Long> resp = sqlProveedor.darIdProductos(pm, nit);
			tx.commit();

			return resp;
		}
		catch (Exception e)
		{
			//	        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}
	/* ****************************************************************
	 * 			Métodos para manejar los SUCURSAL
	 *****************************************************************/

	public Sucursal adicionarSucursal(String idSucursal, double tamañoInstalacion, double nivelReorden, String clave ,long idSupermercado)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long tuplasInsertadas = sqlSucursal.adicionarSucursal(pm, idSucursal, tamañoInstalacion, nivelReorden, idSupermercado);
			tx.commit();
			log.trace ("Inserción de la sucursal: " +idSucursal+ ": " + tuplasInsertadas + " tuplas insertadas");

			return new Sucursal(idSucursal, tamañoInstalacion, nivelReorden, clave, idSupermercado);
		}
		catch (Exception e)
		{
			//	        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public Sucursal darSucursalId(String idSucursal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			Sucursal resp = sqlSucursal.darSucursalPorId(pm, idSucursal);
			tx.commit();

			return resp;
		}
		catch (Exception e)
		{
			//	        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public List<Sucursal> darSucursales()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			List<Sucursal> resp = sqlSucursal.darSucursales(pm);
			tx.commit();

			return resp;
		}
		catch (Exception e)
		{
			//	        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	



	//************************Asociaciones******************************
	//******************************************************************//
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla BEBEDOR
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del bebedor
	 * @param ciudad - La ciudad del bebedor
	 * @param presupuesto - El presupuesto del bebedor (ALTO, MEDIO, BAJO)
	 * @return El objeto BEBEDOR adicionado. null si ocurre alguna Excepción
	 */
	public Promocion_producto registrarPromocionProductos()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idPromocion = nextval();
			long idProducto = nextval();
			long tuplasInsertadas = sqlPromocion_producto.registrarPromocionProductos(pm, idProducto, idPromocion);
			tx.commit();

			log.trace ("Inserción de la promocion: " + idPromocion + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Promocion_producto(idProducto, idPromocion);
		}
		catch (Exception e)
		{
			//    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long darProductosPromocion()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idPromocion = nextval();
			long tuplasInsertadas = sqlPromocion_producto.darProductosPromocion(pm, idPromocion);
			tx.commit();

			return tuplasInsertadas;
		}
		catch (Exception e)
		{
			//    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}

	}

	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla BEBEDOR
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del bebedor
	 * @param ciudad - La ciudad del bebedor
	 * @param presupuesto - El presupuesto del bebedor (ALTO, MEDIO, BAJO)
	 * @return El objeto BEBEDOR adicionado. null si ocurre alguna Excepción
	 */
	public Producto_pedidos registrarProductosPedido()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idProducto = nextval();
			long idPedido = nextval();
			long tuplasInsertadas = sqlProductos_pedidos.registrarProductosPedidos(pm, idProducto, idPedido);
			tx.commit();

			log.trace ("Inserción del Estante: " + idPedido + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Producto_pedidos(idProducto,idPedido);
		}
		catch (Exception e)
		{
			//    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long darProductosPedido()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idPedido = nextval();
			long tuplasInsertadas = sqlProductos_pedidos.darProductosPedido(pm, idPedido);
			tx.commit();

			return tuplasInsertadas;
		}
		catch (Exception e)
		{
			//    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}

	}

	public producto_proveedor registrarProductosProveedor()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idProducto = nextval();
			long idProveedor = nextval();
			long tuplasInsertadas = sqlProducto_proveedor.registrarProductosProveedor(pm, idProducto, idProveedor);
			tx.commit();

			log.trace ("Inserción del Estante: " + idProveedor + ": " + tuplasInsertadas + " tuplas insertadas");

			return new producto_proveedor(idProducto, idProveedor);
		}
		catch (Exception e)
		{
			//    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long darProductosProveedor()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idProveedor = nextval();
			long tuplasInsertadas = sqlProducto_proveedor.darProductosProveedor(pm, idProveedor);
			tx.commit();

			return tuplasInsertadas;
		}
		catch (Exception e)
		{
			//    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}

	}

	public Ventas_cliente registrarClientesVentas()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idCliente = nextval();
			long idVenta = nextval();
			long tuplasInsertadas = sqlVentas_cliente.registrarVentasCliente(pm, idVenta, idCliente);
			tx.commit();

			log.trace ("Inserción del Estante: " + idVenta + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Ventas_cliente(idVenta, idCliente);
		}
		catch (Exception e)
		{
			//    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}

	public long darVentasClientes()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idVenta = nextval();
			long tuplasInsertadas = sqlVentas_cliente.darClientesVenta(pm, idVenta);
			tx.commit();

			return tuplasInsertadas;
		}
		catch (Exception e)
		{
			//    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}

	}

	public Ventas_productos registrarProductosVentas()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idProducto = nextval();
			long idVenta = nextval();
			long tuplasInsertadas = sqlVentas_productos.registrarVentasProductos(pm, idVenta, idProducto);
			tx.commit();

			log.trace ("Inserción del Estante: " + idVenta + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Ventas_productos(idVenta, idProducto);
		}
		catch (Exception e)
		{
			//    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}
	
	public long darVentasProductos()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idVenta = nextval();
			long tuplasInsertadas = sqlVentas_productos.darProductosVenta(pm, idVenta);
			tx.commit();

			return tuplasInsertadas;
		}
		catch (Exception e)
		{
			//    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return -1;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}

	}
	
	public Ventas_sucursal registrarSucursalesVentas( String idSucursal)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idVenta = nextval();
			long tuplasInsertadas = sqlVentas_sucursal.registrarVentasSucursal(pm, idVenta, idSucursal);
			tx.commit();

			log.trace ("Inserción del Estante: " + idVenta + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Ventas_sucursal(idVenta, idSucursal);
		}
		catch (Exception e)
		{
			//    	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return null;
		}
		finally
		{
			if (tx.isActive())
			{
				tx.rollback();
			}
			pm.close();
		}
	}
	
	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de Parranderos
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA,
	 * TIPOBEBIDA, BEBEDOR y BAR, respectivamente
	 */
	public long [] limpiarSuperandes ()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long [] resp = sqlUtil.limpiarSuperandes(pm);
            tx.commit ();
            log.info ("Borrada la base de datos");
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return new long[] {-1, -1, -1, -1, -1, -1, -1};
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
		
	}
}
