package superandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superandes.negocio.Carro;

public class SQLProductos_Carro {
	
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
	public SQLProductos_Carro (PersistenciaSuperandes pp)
	{
		this.pp = pp;
	}
	
	public long registrarProductosCarro(PersistenceManager pm, long idProducto, long idCarro)
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaProductosCarro() + "(idProducto, idCarro) values (?,?)");
		q.setParameters(idProducto, idCarro);
		return (Long) q.executeUnique();
	}

	public long darProductosCarro(PersistenceManager pm, long idCarro)
	{
		Query q = pm.newQuery(SQL, "SELECT idProducto FROM" + pp.darTablaProductosCarro() + "WHERE idCarro = ?");
		q.setParameters(idCarro);
		return (Long) q.executeUnique();
	}
	
	public void vaciarCarrito( PersistenceManager pm, long idCarro)
	{
		Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaProductosCarro() + "WHERE idCarro =?");
		q.setParameters(idCarro);
	}

}
