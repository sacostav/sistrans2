package superandes.persistencia;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import superandes.negocio.Sucursal;
import superandes.negocio.Venta;
import superandes.negocio.Ventas_productos;
import superandes.negocio.productosEstantes;
import superandes.negocio.Bodega;
import superandes.negocio.Carro;
import superandes.negocio.Cliente;
import superandes.negocio.Estante;
import superandes.negocio.Pedido;
import superandes.negocio.Producto;
import superandes.negocio.Producto_pedidos;
import superandes.negocio.Promocion;
import superandes.negocio.Promocion_producto;
import superandes.negocio.Proveedor;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import javax.swing.JOptionPane;

public class PersistenciaSuperandes {

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecuciÃ³n
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

	private SQLVenta sqlVenta; 

	private SQLProductos_pedidos sqlProductos_pedidos;

	private SQLVentas_productos sqlVentas_productos;

	private SQLPromocion_producto sqlPromocion_producto;

	private SQLProductosBodegas sqlProductosBodega;

	private SQLProductosEstantes sqlProductosEstante;

	private SQLCarro sqlCarro;

	private SQLProductos_Carro sqlProductos_carro;

	private SQLCarritoCliente sqlCarritoCliente;

	/* ****************************************************************
	 * 			MÃ©todos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/

	/**
	 * Constructor privado con valores por defecto - PatrÃ³n SINGLETON
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
		tablas.add("PROMOCION");
		tablas.add("VENTA");
		tablas.add("PRODUCTOSPEDIDOS");
		tablas.add("VENTAPRODUCTOS");
		tablas.add("PRODUCTOSPROMOCION");
		tablas.add("PRODUCTOSBODEGA");
		tablas.add("PRODUCTOSESTANTE");
		tablas.add("CARRO");
		tablas.add("PRODUCTOS_CARRO");
		tablas.add("CARRITOCLIENTE");
		tablas.add("VENTASPROMOCIONES");

	}

	private PersistenciaSuperandes(JsonObject tableConfig)
	{
		System.out.println("Aquí llego");
		crearClasesSQL();
		tablas = leerNombreTablas(tableConfig);
		//
		//
		//		String unidadPersistencia = tableConfig.get ("unidadPersistencia").getAsString ();
		//		System.out.println("la unidad es: " + unidadPersistencia);
		//		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory ("Superandes");
		System.out.println("Aquí también");
	}
	public static PersistenciaSuperandes getInstance() {

		if(instance == null)
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
		sqlProductos_pedidos = new SQLProductos_pedidos(this);
		sqlVentas_productos = new SQLVentas_productos(this);
		sqlPromocion_producto = new SQLPromocion_producto(this);
		sqlProductosBodega = new SQLProductosBodegas(this);
		sqlProductosEstante = new SQLProductosEstantes(this);
		sqlCarro = new SQLCarro(this);
		sqlProductos_carro = new SQLProductos_Carro(this);
		sqlCarritoCliente = new SQLCarritoCliente(this);

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

	public String darTablaCarro(){
		return tablas.get(16);
	}

	public String darTablaProductosCarro(){
		return tablas.get(17);
	}

	public String darTablaCarritoCliente(){
		return tablas.get(18);
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
	 * 			MÃ©todos para manejar los BODEGA
	 *****************************************************************/
	public Bodega adicionarBodega(String categoria, double peso, double volumen, String idSucursal, double nivelAbastecimiento)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{   long idBodega = nextval();
		tx.begin();
		long tuplasInsertadas = sqlBodega.adicionarBodega(pm, idBodega, peso, volumen, categoria, idSucursal,  nivelAbastecimiento);
		tx.commit();

		log.trace ("InserciÃ³n de bodega: " + categoria + ": " + tuplasInsertadas + " tuplas insertadas");

		return new Bodega(idBodega,peso, volumen, categoria,idSucursal, nivelAbastecimiento);
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

	public List<Bodega> darBodegas()
	{
		return sqlBodega.darBodegas(pmf.getPersistenceManager());
	}

	public long eliminarBodegaId(long id)
	{
		return sqlBodega.eliminarBodega(pmf.getPersistenceManager(), id);
	}

	/* ****************************************************************
	 * 			MÃ©todos para manejar los CLIENTES
	 *****************************************************************/


	//RF3

	public Cliente adicionarCliente(int documento, String nombre, int NIT, String correo, String direccion,String tipo)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{   long idCliente = nextval();
		tx.begin();
		long tuplasInsertadas = sqlCliente.registrarCliente(pm, idCliente, documento, NIT, nombre, correo, direccion, tipo);
		tx.commit();

		log.trace ("Insercion de cliente: " + idCliente + ": " + tuplasInsertadas + " tuplas insertadas");

		return new Cliente(idCliente, documento, NIT, nombre, correo, direccion, tipo);
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


	public List<Cliente> darClientes()
	{
		return sqlCliente.darClientes(pmf.getPersistenceManager());
	}

	public long eliminarClienteId(long id)
	{
		return sqlCliente.eliminarClienteId(pmf.getPersistenceManager(), id);
	}

	/* ****************************************************************
	 * 			Metodos para manejar los ESTANTE
	 *****************************************************************/
	/**
	 * 
	 * @param nivelAbastecimiento
	 * @param peso
	 * @param volumen
	 * @param categoria
	 * @param idSucursal
	 * @return
	 */

	//FIXME RF6 

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

			log.trace ("InserciÃ³n del estante: " + categoria+ "-"+idSucursal+ ": " + tuplasInsertadas + " tuplas insertadas");

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

	public List<String> productosEstante()
	{
		return sqlEstante.cantidadProductosEnEstante(pmf.getPersistenceManager());
	}

	public List<Estante> darEstantes()
	{
		return sqlEstante.darEstantes(pmf.getPersistenceManager());
	}

	public Estante darEstanteId(long id)
	{
		return sqlEstante.darEstanteId(pmf.getPersistenceManager(), id);
	}

	public long eliminarEstanteId(long id)
	{
		return sqlEstante.eliminarEstanteId(pmf.getPersistenceManager(), id);
	}


	/* ****************************************************************
	 * 			MÃ©todos para manejar los PEDIDOS
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

			log.trace ("InserciÃ³n del pedido: " +idPedido+ ": " + tuplasInsertadas + " tuplas insertadas");

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


	public List<Pedido> darPedidos()
	{
		return sqlPedido.darPedidos(pmf.getPersistenceManager());
	}

	public long eliminarPedidoId(long id)
	{
		return sqlPedido.eliminarPedidoId(pmf.getPersistenceManager(), id);
	}

	public Pedido darPedidoId(long id)
	{
		return sqlPedido.darPedidoId(pmf.getPersistenceManager(), id);
	}

	/* ****************************************************************
	 * 			Metodos para manejar los PRODUCTOS
	 *****************************************************************/

	// RF2
	public Producto adicionarProducto(String nombre, String marca, double precioUnitario, double precioUnidadMedida, String unidadMed, double volumenEmpaque, double pesoEmpaque, String codigoBarras, Date fechaVencimiento, int nivelReorden, double precioProveedor, long idProveedor, long idBodega)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idProducto = nextval ();
			long tuplasInsertadas = sqlProducto.registrarProducto(pm, idProducto, nombre, marca, precioUnitario, precioUnidadMedida, unidadMed, volumenEmpaque, pesoEmpaque, codigoBarras, fechaVencimiento, nivelReorden, precioProveedor, idProveedor);
			tx.commit();
			
			long tuplasInsertadas2 = sqlProductosBodega.registrarProductoEnBodega(pm, idProducto, idBodega);
            log.trace("Inserción del producto" + idProducto+"-"+idBodega+""+tuplasInsertadas2+"tuplas insertadas");
			
			log.trace ("Inserción del productoBodega: " +idProducto+ ": " + tuplasInsertadas + " tuplas insertadas");

			log.trace ("Insercion del producto: " +idProducto+ ": " + tuplasInsertadas + " tuplas insertadas");


			return new Producto (idProducto, nombre, marca, precioUnitario, precioUnidadMedida, unidadMed, volumenEmpaque, pesoEmpaque, codigoBarras, fechaVencimiento, nivelReorden, precioProveedor, idProveedor);
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

	
	
	
	
	//--------------------------------------------------------
	// Metodos para manejar la asociacion productos estante
	//--------------------------------------------------------
	
	public Producto adicionarProductoEstante(String nombre, String marca, double precioUnitario, double precioUnidadMedida, String unidadMed, double volumenEmpaque, double pesoEmpaque, String codigoBarras, Date fechaVencimiento, int nivelReorden, double precioProveedor, long idProveedor, long idBodega)


	public Producto darProductoId(long id) {
		return sqlProducto.darProductoId(pmf.getPersistenceManager(), id);
	}

	public List<Long> darIdProductos(int nit)

	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();

			long idProducto = nextval ();
			long tuplasInsertadas = sqlProducto.registrarProducto(pm, idProducto, nombre, marca, precioUnitario, precioUnidadMedida, unidadMed, volumenEmpaque, pesoEmpaque, codigoBarras, fechaVencimiento, nivelReorden, precioProveedor, idProveedor);

			List<Long> resp = sqlProveedor.darIdProductos(pm, nit);

			tx.commit();
			
			long tuplasInsertadas2 = sqlProductosBodega.registrarProductoEnBodega(pm, idProducto, idBodega);
            log.trace("Inserción del producto" + idProducto+"-"+idBodega+""+tuplasInsertadas2+"tuplas insertadas");
			
			log.trace ("Inserción del productoBodega: " +idProducto+ ": " + tuplasInsertadas + " tuplas insertadas");


			return new Producto (idProducto, nombre, marca, precioUnitario, precioUnidadMedida, unidadMed, volumenEmpaque, pesoEmpaque, codigoBarras, fechaVencimiento, nivelReorden, precioProveedor, idProveedor);


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


	public long eliminarProductoId(long id)
	{
		return sqlProducto.eliminarProductoId(pmf.getPersistenceManager(), id);
	}

	public List<Producto> darProductos()
	{
		return sqlProducto.darProductos(pmf.getPersistenceManager());
	}

	//----------------------------------------------------------------
	// Metodos para manejar la asociacion productosEstante
	//----------------------------------------------------------------

	public productosEstantes adicionarProductosEstante(long idProducto, long idEstante)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long idCliente = nextval ();
			long tuplasInsertadas = sqlProductosEstante.adicionarProductosEstante(pm, idEstante, idProducto);
			tx.commit();

			log.trace ("InserciÃ³n de cliente: " + idProducto+"-"+ idEstante+ ": " + tuplasInsertadas + " tuplas insertadas");

			return new productosEstantes(idProducto, idEstante);
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




	/* ****************************************************************
	 * 			MÃ©todos para manejar los PROMOCION
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

			log.trace ("InserciÃ³n del producto: " +idPromocion + ": " + tuplasInsertadas + " tuplas insertadas");

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

	public List<Promocion> darPromociones()
	{
		return sqlPromocion.darPromociones(pmf.getPersistenceManager());
	}

	public long eliminarPromocionId(long id)
	{
		return sqlPromocion.eliminarPromocionId(pmf.getPersistenceManager(), id);
	}


	/* ****************************************************************
	 * 			MÃ©todos para manejar los PROVEEDOR
	 *****************************************************************/
	public Proveedor adicionarProveedor(int nit,String nombre, double calificacion)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long tuplasInsertadas = sqlProveedor.adicionarProveedor(pm, nit, nombre, calificacion);
			tx.commit();

			log.trace ("InserciÃ³n del producto: " +nit + ": " + tuplasInsertadas + " tuplas insertadas");

			return new Proveedor(nit, nombre, calificacion);
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
			List<Proveedor> resp = sqlProveedor.darProveedor(pm);
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

	public long eliminarProveedorId(long id)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long eliminado = sqlProveedor.eliminarProveedorId(pmf.getPersistenceManager(), id);
			tx.commit();

			return eliminado;
		}
		catch (Exception e)
		{
			//	        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return 0;
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
	 * 			MÃ©todos para manejar los SUCURSAL
	 *****************************************************************/

	public Sucursal adicionarSucursal(String id, double tamanio, String clave )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long tuplasInsertadas = sqlSucursal.adicionarSucursal(pm, id, tamanio, clave);
			tx.commit();
			log.trace ("InserciÃ³n de la venta: " +id+ ": " + tuplasInsertadas + " tuplas insertadas");

			return new Sucursal(id, tamanio, clave);
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

	public long eliminarSucursalId( String id)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlSucursal.eliminarSucursalId(pmf.getPersistenceManager(), id);
			tx.commit();

			return resp;
		}
		catch (Exception e)
		{
			//	        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return 0;
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
	 * 			MÃ©todos para manejar las VENTAS
	 *****************************************************************/
	public Venta adicionarVenta(long id, double total, long idCliente, String idSucursal )
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long tuplasInsertadas = sqlVenta.adicionarVenta(pm, id, total, idCliente, idSucursal);
			tx.commit();
			log.trace ("InserciÃ³n de la venta: " +id+ ": " + tuplasInsertadas + " tuplas insertadas");

			return new Venta(id, total, idCliente, idSucursal);
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

	public Venta darVentaId(long id)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			Venta resp = sqlVenta.darVentaPorId(pm, id);
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

	public List<Venta> darVentas()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			List<Venta> resp = sqlVenta.darVentas(pm);
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

	public long eliminarVentaId(long id)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx=pm.currentTransaction();
		try
		{
			tx.begin();
			long resp = sqlVenta.eliminarVentaId(pmf.getPersistenceManager(), id);
			tx.commit();

			return resp;
		}
		catch (Exception e)
		{
			//	        	e.printStackTrace();
			log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
			return 0;
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
	 * MÃ©todo que inserta, de manera transaccional, una tupla en la tabla BEBEDOR
	 * Adiciona entradas al log de la aplicaciÃ³n
	 * @param nombre - El nombre del bebedor
	 * @param ciudad - La ciudad del bebedor
	 * @param presupuesto - El presupuesto del bebedor (ALTO, MEDIO, BAJO)
	 * @return El objeto BEBEDOR adicionado. null si ocurre alguna ExcepciÃ³n
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

			log.trace ("InserciÃ³n de la promocion: " + idPromocion + ": " + tuplasInsertadas + " tuplas insertadas");

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
	 * MÃ©todo que inserta, de manera transaccional, una tupla en la tabla BEBEDOR
	 * Adiciona entradas al log de la aplicaciÃ³n
	 * @param nombre - El nombre del bebedor
	 * @param ciudad - La ciudad del bebedor
	 * @param presupuesto - El presupuesto del bebedor (ALTO, MEDIO, BAJO)
	 * @return El objeto BEBEDOR adicionado. null si ocurre alguna ExcepciÃ³n
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

			log.trace ("InserciÃ³n del Estante: " + idPedido + ": " + tuplasInsertadas + " tuplas insertadas");

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


	/* ****************************************************************
	 * 			MÃ©todos para manejar Ventas de productos
	 *****************************************************************/
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

			log.trace ("InserciÃ³n del Estante: " + idVenta + ": " + tuplasInsertadas + " tuplas insertadas");

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

	/* ****************************************************************
	 * 			MÃ©todos para manejar Solicitud carro de compras
	 *****************************************************************/

	public void eliminarProductoEstante( long idProducto)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		try{
			tx.begin();
			sqlProductosEstante.eliminarProductosdelEstante(pm, idProducto);

		} catch ( Exception e)
		{
			JOptionPane.showInternalMessageDialog(null, "No se pudo eliminar el producto");
		}
	}
	
	public void eliminarProductoBodega( long idProducto)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		try{
			tx.begin();
			sqlProductosBodega.eliminarProductoEnBodegaPorId(pm, idProducto);

		} catch ( Exception e)
		{
			JOptionPane.showInternalMessageDialog(null, "No se pudo eliminar el producto");
		}
	}
	
	//FIXME RF13
	public void adicionarProductoCarro( long idCliente , long idProducto)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try{
			Carro carro = sqlCarritoCliente.darCarroCliente(pm, idCliente);
			if( carro != null)
			{
				long idCarro = carro.getIdCarro();
				sqlProductos_carro.registrarProductosCarro(pm, idProducto, idCarro);
				Estante estante = sqlProductosEstante.darEstanteProducto(pm, idProducto);
				if( estante != null)
				{
					long idEstante = estante.getIdEstante();
					sqlProductosEstante.eliminarProductosdelEstante(pm, idProducto);
				}
				
			}else
				solicitarCarro(idCliente);
		} catch (Exception e ){
			JOptionPane.showMessageDialog(null, "No se pudo adicionar un producto al carro ");
		}
	}


	/* ****************************************************************
	 * 			MÃ©todos para manejar Solicitud carro de compras
	 *****************************************************************/
	// FIXME RF12
	public Carro solicitarCarro(long idCliente)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			long idCarro = nextval();
			if( sqlProductos_carro.darProductosCarro(pm, idCarro).isEmpty())
			{
				sqlCarritoCliente.registrarCarritoCliente(pm, idCliente, idCarro);
				return sqlCarro.darCarroPorId( pm, idCarro);
			}
		}catch(Exception e){
			JOptionPane.showMessageDialog(null, "No ha sido posible asignarle un carro");
		}

		return null;
	}

	// FIXME RF14
	public Producto devolverProducto(long idCliente, long idProducto)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();

		try{
			tx.begin();
			Carro carro = sqlCarritoCliente.darCarroCliente(pm, idCliente);
			if( carro != null){
				long idCarro = carro.getIdCarro();
				sqlProductos_carro.eliminarProductoCarrito(pm, idProducto);
				Estante estante = sqlProductosEstante.darEstanteProducto(pm, idProducto);
				if( estante != null){
					long idEstante = estante.getIdEstante();
					sqlProductosEstante.adicionarProductosEstante(pm, idEstante, idProducto);
				}

			}

		}catch (Exception e){
			JOptionPane.showInternalMessageDialog(null, "El producto no se pudo devolver");
		}
		
		return sqlProducto.darProductoId(pm, idProducto);
	}
	
	// FIXME RF15
	public List<Object[]> pagarCompra(long idProducto)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		List<Object[]> rta = new LinkedList() ;
		
		try{
			eliminarEstanteId(idProducto);
 		}
	}

	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de Parranderos
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @return Un arreglo con 7 nÃºmeros que indican el nÃºmero de tuplas borradas en las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA,
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
