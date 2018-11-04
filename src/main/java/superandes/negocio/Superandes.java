package superandes.negocio;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gson.JsonObject;

import superandes.persistencia.PersistenciaSuperandes;

public class Superandes {


	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(Superandes.class.getName());

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaSuperandes ps;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public Superandes ()
	{
		ps = PersistenciaSuperandes.getInstance ();
	}

	/**
	 * El constructor qye recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public Superandes (JsonObject tableConfig)
	{
		ps = PersistenciaSuperandes.getInstance (tableConfig);
	}

	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia ()
	{
		ps.cerrarUnidadPersistencia ();
	}

	/* *************************************************
	 * Metodos para manejar las bodegas
	 ***************************************************/

	/**
	 * Adiciona de manera persistente una bodega
	 * Adiciona entradas al log de la aplicación
	 * @param id- El id de la bodega
	 * @return El objeto bodega adicionado. null si ocurre alguna Excepción
	 */
	public Bodega adicionarBodega(long id, double peso,double volumen,String categoria, String idSucurssal, double nivelAbastecimiento)
	{
		log.info ("Adicionando bodega: " + id);
		Bodega bodega = ps.adicionarBodega(categoria, peso, volumen, idSucurssal, nivelAbastecimiento); 		
		log.info ("Adicionando cliente: " + bodega);
		return bodega;
	}
	
	/**
	 * Encuentra el tipos de bebida en Parranderos con el nombre solicitado
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre de la bebida
	 * @return Un objeto TipoBebida con el tipos de bebida de ese nombre que conoce la aplicación, 
	 * lleno con su información básica
	 */
	public Bodega darBodegaPorId (long id)
	{
		log.info ("Buscando bodega por id: " + id);
		Bodega tb = ps.darBodegaPorId(id);
		return tb;
	}

	/**
	 * Encuentra todos los tipos de bebida en Parranderos y los devuelve como una lista de VOTipoBebida
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOTipoBebida con todos los tipos de bebida que conoce la aplicación, llenos con su información básica
	 */
	public List<VOBodega> darVOBodega ()
	{
		log.info ("Generando los VO de Bodega");        
        List<VOBodega> voTipos = new LinkedList<VOBodega>();
        for (Bodega tb : ps.darBodegas ())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Bodega: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	/**
	 * Elimina una bebida por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param id - El nombre de la bebida a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarBodega (long id)
	{
        log.info ("Eliminando bodega por id: " + id);
        long resp = ps.eliminarBodegaId(id);
        log.info ("Eliminando bodega por id: " + resp + " tuplas eliminadas");
        return resp;
	}

	/* *************************************************
	 * Metodos para manejar los clientes
	 ***************************************************/
	/**
	 * Adiciona de manera persistente un tipo de bebida 
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del tipo de bebida
	 * @return El objeto TipoBebida adicionado. null si ocurre alguna Excepción
	 */
	public Cliente registrarCliente(int documento, int nit, String nombre, String direccion, String correo, String tipo)
	{
		log.info ("Adicionando cliente: " + nombre);
		Cliente cliente = ps.adicionarCliente(documento, nombre, nit, correo, direccion, tipo); 		
		log.info ("Adicionando cliente: " + cliente);
		return cliente;
	}

	/**
	 * Encuentra el tipos de bebida en Parranderos con el nombre solicitado
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre de la bebida
	 * @return Un objeto TipoBebida con el tipos de bebida de ese nombre que conoce la aplicación, 
	 * lleno con su información básica
	 */
	public Cliente darClientePorId (long idCliente)
	{
		log.info ("Buscando cliente por id: " + idCliente);
		Cliente tb = ps.darClienteId(idCliente);
		return tb;
	}

	/**
	 * Encuentra todos los tipos de bebida en Parranderos y los devuelve como una lista de VOTipoBebida
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOTipoBebida con todos los tipos de bebida que conoce la aplicación, llenos con su información básica
	 */
	public List<VOCliente> darVOCliente ()
	{
		log.info ("Generando los VO de Cliente");        
        List<VOCliente> voTipos = new LinkedList<VOCliente>();
        for (Cliente tb : ps.darClientes())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Cliente: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	/**
	 * Elimina una bebida por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param id - El nombre de la bebida a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarCliente (long id)
	{
        log.info ("Eliminando cliente por id: " + id);
        long resp = ps.eliminarClienteId(id);
        log.info ("Eliminando cliente por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/* *************************************************
	 * Metodos para manejar los estantes
	 ***************************************************/

	/**
	 * Adiciona de manera persistente un tipo de bebida 
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del tipo de bebida
	 * @return El objeto TipoBebida adicionado. null si ocurre alguna Excepción
	 */
	public Estante registrarEstante(int nivel, double volumen, double peso, String categoria, String idSucursal)
	{
		log.info ("Adicionando estante: " + nivel + volumen + peso);
		Estante estante = ps.adicionarEstante(nivel, peso, volumen, categoria, idSucursal);
		log.info ("Adicionando estante: " + estante);
		return estante;
	}


	public Estante darEstanteSucursal(String idSucursal, String categoria)
	{
		log.info ("Adicionando estante: " + idSucursal + categoria);
		Estante estante = ps.darEstantePorSucursalyCategoria(idSucursal, categoria);
		log.info ("Adicionando estante: " + estante);
		return estante;
	}
	
	/**
	 * Encuentra el tipos de bebida en Parranderos con el nombre solicitado
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre de la bebida
	 * @return Un objeto TipoBebida con el tipos de bebida de ese nombre que conoce la aplicación, 
	 * lleno con su información básica
	 */
	public Estante darEstantePorId (long id)
	{
		log.info ("Buscando estante por id: " + id);
		Estante tb = ps.darEstanteId(id);
		return tb;
	}
	
	/**
	 * Elimina una bebida por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param id - El nombre de la bebida a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarEstante (long id)
	{
        log.info ("Eliminando estante por id: " + id);
        long resp = ps.eliminarEstanteId(id);
        log.info ("Eliminando estante por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todos los tipos de bebida en Parranderos y los devuelve como una lista de VOTipoBebida
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOTipoBebida con todos los tipos de bebida que conoce la aplicación, llenos con su información básica
	 */
	public List<VOEstante> darVOEstante ()
	{
		log.info ("Generando los VO de Cliente");        
        List<VOEstante> voTipos = new LinkedList<VOEstante>();
        for (Estante tb : ps.darEstantes())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Cliente: " + voTipos.size() + " existentes");
        return voTipos;
	}

	/* *************************************************
	 * Metodos para manejar los pedidos
	 ***************************************************/
	/**
	 * Adiciona de manera persistente un tipo de bebida 
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del tipo de bebida
	 * @return El objeto TipoBebida adicionado. null si ocurre alguna Excepción
	 */
	public Pedido registrarPedido(Date fechaPedido, Date fechaLlegada, String idSucursal, long idProveedor, String estadoPedido)
	{
		log.info ("Adicionando pedido: " + fechaPedido + fechaLlegada + idSucursal + idProveedor);
		Pedido pedido= ps.adicionarPedido(fechaPedido, fechaLlegada, idSucursal, idProveedor, estadoPedido);
		log.info ("Adicionando pedido: " + pedido);
		return pedido;
	}

	public long registrarLlegadaPedido(Date fechaLlegada, long idPedido, long idBodega)
	{
		log.info("Registrando llegada de un pedido: " + fechaLlegada + idPedido);
		long resp = ps.registrarLlegadaPedido(idPedido, fechaLlegada);
		log.info("Registrando fecha llegada pedido: " + resp);
		return resp;
	}
	
	/**
	 * Encuentra el tipos de bebida en Parranderos con el nombre solicitado
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre de la bebida
	 * @return Un objeto TipoBebida con el tipos de bebida de ese nombre que conoce la aplicación, 
	 * lleno con su información básica
	 */
	public Pedido darPedidoPorId (long id)
	{
		log.info ("Buscando pedido por id: " + id);
		Pedido tb = ps.darPedidoId(id);
		return tb;
	}

	/**
	 * Elimina una bebida por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param id - El nombre de la bebida a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarPedido (long id)
	{
        log.info ("Eliminando pedido por id: " + id);
        long resp = ps.eliminarPedidoId(id);
        log.info ("Eliminando pedido por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todos los tipos de bebida en Parranderos y los devuelve como una lista de VOTipoBebida
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOTipoBebida con todos los tipos de bebida que conoce la aplicación, llenos con su información básica
	 */
	public List<VOPedido> darVOPedido ()
	{
		log.info ("Generando los VO de Cliente");        
        List<VOPedido> voTipos = new LinkedList<VOPedido>();
        for (Pedido tb : ps.darPedidos())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Cliente: " + voTipos.size() + " existentes");
        return voTipos;
	}

	/* *************************************************
	 * Metodos para manejar los productos
	 ***************************************************/

	public Producto adicionarProducto(long id, String nombre, String marca, double precioUnitario, double precioUnidas, String unidadMed, double volumen, double peso, String codigo, Date fecha, int nivel, double precio, long idProveedor)
	{
		log.info("Adicionando producto: " + id + nombre + marca + precioUnitario + precioUnidas + unidadMed+ volumen+ peso+ codigo+ fecha+ nivel+ precio+ idProveedor);
		Producto producto = ps.adicionarProducto(nombre, marca, precioUnitario, precioUnidas, unidadMed, volumen, peso, codigo, fecha, nivel, precio, idProveedor);
		log.info("Adicionando producto: " + producto);
		return producto;
	}
	
	/**
	 * Elimina una bebida por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param id - El nombre de la bebida a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarProducto (long id)
	{
        log.info ("Eliminando producto por id: " + id);
        long resp = ps.eliminarProductoId(id);
        log.info ("Eliminando producto por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra el tipos de bebida en Parranderos con el nombre solicitado
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre de la bebida
	 * @return Un objeto TipoBebida con el tipos de bebida de ese nombre que conoce la aplicación, 
	 * lleno con su información básica
	 */
	public Producto darProductoPorId (long id)
	{
		log.info ("Buscando producto por id: " + id);
		Producto tb = ps.darProductoId(id);
		return tb;
	}
	
	/**
	 * Encuentra todos los tipos de bebida en Parranderos y los devuelve como una lista de VOTipoBebida
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOTipoBebida con todos los tipos de bebida que conoce la aplicación, llenos con su información básica
	 */
	public List<VOProducto> darVOProducto ()
	{
		log.info ("Generando los VO de Cliente");        
        List<VOProducto> voTipos = new LinkedList<VOProducto>();
        for (Producto tb : ps.darProductos())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Cliente: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	/* *************************************************
	 * Metodos para manejar los promocion
	 ***************************************************/
	
	public Promocion adicionarPromocion(long id, String tipo, Date fechaInicio, Date fechaFin)
	{
		log.info("Adicionando promocion: " + id);
		Promocion promocion = ps.adicionarPromocion(tipo, fechaFin, fechaInicio);
		log.info("Adicionando promocion: " + promocion);
		return promocion;
	}
	
	public List<Promocion> darPromociones()
	{
		return ps.darPromociones();
	}
	
	/**
	 * Elimina una bebida por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param id - El nombre de la bebida a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarPromocion (long id)
	{
        log.info ("Eliminando promocion por id: " + id);
        long resp = ps.eliminarPromocionId(id);
        log.info ("Eliminando promocion por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra el tipos de bebida en Parranderos con el nombre solicitado
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre de la bebida
	 * @return Un objeto TipoBebida con el tipos de bebida de ese nombre que conoce la aplicación, 
	 * lleno con su información básica
	 */
	public Promocion darPromocionPorId (long id)
	{
		log.info ("Buscando promocion por id: " + id);
		Promocion tb = ps.darPromocionId(id);
		return tb;
	}
	
	/**
	 * Encuentra todos los tipos de bebida en Parranderos y los devuelve como una lista de VOTipoBebida
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOTipoBebida con todos los tipos de bebida que conoce la aplicación, llenos con su información básica
	 */
	public List<VOPromocion> darVOPromocion ()
	{
		log.info ("Generando los VO de Cliente");        
        List<VOPromocion> voTipos = new LinkedList<VOPromocion>();
        for (Promocion tb : ps.darPromociones())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Cliente: " + voTipos.size() + " existentes");
        return voTipos;
	}

	/* *************************************************
	 * Metodos para manejar los proveedores
	 ***************************************************/
	public Proveedor adicionarProveedor(int nit, String nombre, double calificacion)
	{
		log.info("Adicionando proveedor : " + nit);
		Proveedor proveedor = ps.adicionarProveedor(nit, nombre, calificacion);
		log.info("Adicionando proveedor: " + proveedor);
		return proveedor;
	}
	
	public Proveedor darProveedorId(int id)
	{
		log.info("Buscando proveedor: " + id);
		Proveedor proveedor = ps.darProveedorNit(id);
		log.info("Buscando proveedor: " + proveedor);
		return proveedor;
	}
	
	/**
	 * Elimina una bebida por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param id - El nombre de la bebida a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarProveedor (long id)
	{
        log.info ("Eliminando proveedor por id: " + id);
        long resp = ps.eliminarProveedorId(id);
        log.info ("Eliminando proveedor por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	
	/**
	 * Encuentra todos los tipos de bebida en Parranderos y los devuelve como una lista de VOTipoBebida
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOTipoBebida con todos los tipos de bebida que conoce la aplicación, llenos con su información básica
	 */
	public List<VOProveedor> darVOProveedor ()
	{
		log.info ("Generando los VO de Proveedor");        
        List<VOProveedor> voTipos = new LinkedList<VOProveedor>();
        for (Proveedor tb : ps.darProveedores())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Proveedor: " + voTipos.size() + " existentes");
        return voTipos;
	}

	/* *************************************************
	 * Metodos para manejar las sucursales
	 ***************************************************/

	public Sucursal adicionarSucursal( String id, double tamanio, String clave)
	{
		log.info("Adicionando sucursal : " + id);
		Sucursal sucursal = ps.adicionarSucursal(id, tamanio, clave);
		log.info("Adicionando sucursal : " + sucursal);
		return sucursal;
	}
	
	public Sucursal darSucursalId(String id)
	{
		log.info("Buscando sucursal :" + id);
		Sucursal sucursal = ps.darSucursalId(id);
		log.info("buscando sucursal : " + sucursal);
		return sucursal;
	}
	
	/**
	 * Elimina una bebida por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param id - El nombre de la bebida a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarSucursal (String id)
	{
        log.info ("Eliminando sucursal por id: " + id);
        long resp = ps.eliminarSucursalId(id);
        log.info ("Eliminando sucursal por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todos los tipos de bebida en Parranderos y los devuelve como una lista de VOTipoBebida
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOTipoBebida con todos los tipos de bebida que conoce la aplicación, llenos con su información básica
	 */
	public List<VOSucursal> darVOSucursal ()
	{
		log.info ("Generando los VO de Cliente");        
        List<VOSucursal> voTipos = new LinkedList<VOSucursal>();
        for (Sucursal tb : ps.darSucursales())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Cliente: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	/* *************************************************
	 * Metodos para manejar los ventas
	 ***************************************************/

	public Venta adicionarVenta(long id, double total, long idCliente, String idSucursal)
	{
		log.info("Adicionando venta " + id);
		Venta venta = ps.adicionarVenta(id, total, idCliente, idSucursal);
		log.info("Adicionando venta: " + venta);
		return venta;
	}
	
	public Venta darVentaId(long id)
	{
		log.info("Buscando venta" + id);
		Venta venta = ps.darVentaId(id);
		log.info("Bsucando venta" + venta);
		return venta;
	}
	
	/**
	 * Elimina una bebida por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param id - El nombre de la bebida a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarVenta (long id)
	{
        log.info ("Eliminando venta por id: " + id);
        long resp = ps.eliminarVentaId(id);
        log.info ("Eliminando venta por id: " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Encuentra todos los tipos de bebida en Parranderos y los devuelve como una lista de VOTipoBebida
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos VOTipoBebida con todos los tipos de bebida que conoce la aplicación, llenos con su información básica
	 */
	public List<VOVenta> darVOVenta ()
	{
		log.info ("Generando los VO de Cliente");        
        List<VOVenta> voTipos = new LinkedList<VOVenta>();
        for (Venta tb : ps.darVentas())
        {
        	voTipos.add (tb);
        }
        log.info ("Generando los VO de Cliente: " + voTipos.size() + " existentes");
        return voTipos;
	}
	
	/* ****************************************************************
	 * 			Métodos para administración
	 *****************************************************************/

	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de Parranderos
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA,
	 * TIPOBEBIDA, BEBEDOR y BAR, respectivamente
	 */
	public long [] limpiarSuperandes ()
	{
        log.info ("Limpiando la BD de Superandes");
        long [] borrrados = ps.limpiarSuperandes();	
        log.info ("Limpiando la BD de Superandes: Listo!");
        return borrrados;
	}
	
}