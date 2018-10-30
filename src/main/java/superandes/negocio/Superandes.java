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

	/* *************************************************
	 * Metodos para manejar los proveedores
	 ***************************************************/
	public Proveedor adicionarProveedor(int nit, String nombre, int calificacion)
	{
		log.info("Adicionando proveedor : " + nit);
		Proveedor proveedor = ps.adicionarProveedor(nit, nombre, calificacion);
		log.info("Adicionando proveedor: " + proveedor);
		return proveedor;
	}

	/* *************************************************
	 * Metodos para manejar las sucursales
	 ***************************************************/

	/* *************************************************
	 * Metodos para manejar los ventas
	 ***************************************************/

}