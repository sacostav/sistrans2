package superandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLProductosEstantes {


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
	public SQLProductosEstantes (PersistenciaSuperandes pp)
	{
		this.pp = pp;
	}

	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una BODEGA a la base de datos de SUPERANDES
	 * @param pm - El manejador de persistencia
	 * @param idEstante - El identificador de la bodega
	 * @param idProducto - categoria producto 
	 */

	public long adicionarProductosEstante (PersistenceManager pm, long idEstante, long idProducto) 
	
	{
	    Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaProductosEstante() + "(idProducto, idEstante) values (?, ?)");
		q.setParameters(idProducto, idEstante);
		return (long) q.executeUnique();
	}
	
	public long eliminarProductosdelEstante (PersistenceManager pm, long idProd)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaProductosEstante() + " WHERE idproducto = ?");
        q.setParameters(idProd);
        return (long) q.executeUnique();            
    }
}
