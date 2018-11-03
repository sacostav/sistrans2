package superandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLCarritoCliente {
	
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
	public SQLCarritoCliente (PersistenciaSuperandes pp)
	{
		this.pp = pp;
	}
	
	public long registrarCarritoCliente(PersistenceManager pm, long idCliente, long idCarro)
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCarritoCliente() + "(idCliente, idCarro) values (?,?)");
		q.setParameters(idCliente, idCarro);
		return (Long) q.executeUnique();
	}


}
