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
	
	
	/* *************************************************
	 * Metodos para manejar los clientes
	 ***************************************************/
	/**
	 * Adiciona de manera persistente un tipo de bebida 
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del tipo de bebida
	 * @return El objeto TipoBebida adicionado. null si ocurre alguna Excepción
	 */
	public Cliente registrarCliente(int documento, int nit, String nombre, String direccion, String correo, String tipo, long idSup)
	{
        log.info ("Adicionando cliente: " + nombre);
        Cliente cliente = ps.adicionarCliente(documento, nombre, nit, correo, direccion, tipo, idSup); 		
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
	
	/* *************************************************
	 * Metodos para manejar los proveedores
	 ***************************************************/
	
	/* *************************************************
	 * Metodos para manejar las sucursales
	 ***************************************************/
	
	/* *************************************************
	 * Metodos para manejar los supermercados
	 ***************************************************/
	/**
	 * Adiciona de manera persistente un tipo de bebida 
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del tipo de bebida
	 * @return El objeto TipoBebida adicionado. null si ocurre alguna Excepción
	 */
	public Supermercado registrarSupermercado()
	{
        log.info ("Adicionando supermercado ");
        Supermercado supermercado= ps.registrarSupermercado();
        log.info ("Adicionando superercado: " + supermercado);
        return supermercado;
	}
	
	/**
	 * Elimina un tipo de bebida por su nombre
	 * Adiciona entradas al log de la aplicación
	 * @param nombre - El nombre del tipo de bebida a eliminar
	 * @return El número de tuplas eliminadas
	 */
	public long eliminarSupermercadoId(long idSupermercado)
	{
		log.info ("Eliminando supermercado: " + idSupermercado);
        long resp = ps.eliminarSupermercadoId(idSupermercado);
        log.info ("Eliminando supermercado : " + resp + " tuplas eliminadas");
        return resp;
	}
	
	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de Parranderos
	 * @return Un arreglo con 7 números que indican el número de tuplas borradas en las tablas GUSTAN, SIRVEN, VISITAN, BEBIDA,
	 * TIPOBEBIDA, BEBEDOR y BAR, respectivamente
	 */
	public long [] limpiarSuperandes()
	{
        log.info ("Limpiando la BD de Parranderos");
        long [] borrrados = ps.limpiarSuperandes();	
        log.info ("Limpiando la BD de Parranderos: Listo!");
        return borrrados;
	}
}