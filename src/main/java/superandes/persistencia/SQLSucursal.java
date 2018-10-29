package superandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;
import java.util.LinkedList;

import superandes.negocio.Bodega;
import superandes.negocio.Sucursal;

public class SQLSucursal {

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra ac� para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaSuperandes.SQL;

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicaci�n
	 */
	private PersistenciaSuperandes pp;

	/* ****************************************************************
	 * 			M�todos
	 *****************************************************************/
	
	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicaci�n
	 */
	public SQLSucursal (PersistenciaSuperandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una Sucursal a la base de datos de SUPERANDES
	 * @param pm - El manejador de persistencia
	 * @param idSucursal - El identificador de la sucursal
	 * @param tama�oInstalacion - El tama�o de la sucursal 
	 * @param nivelReorden - El nivel de reorden de la sucursal
	 * @param idProveedores - Lista con los id de los proveedores de productos  
	 * @param idSupermercado - id del supermercado al cual pertenece la sucursal 
	 * @return El n�mero de tuplas insertadas
	 */
	
	public long adicionarSucursal (PersistenceManager pm, String idSucursal, double tama�oInstalacion, String clave) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaSucursal () + "(idSucursal, tama�oInstalacion, clave) values (?, ?, ?)");
        q.setParameters(idSucursal, tama�oInstalacion, clave);
        return (long) q.executeUnique();
	}
	
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la informaci�n de una sucursal  
	 * base de datos de Superandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idSucursal - El identificador de la sucursal
	 * @return El objeto Sucursal que tiene el identificador dado
	 */
	public Sucursal darSucursalPorId (PersistenceManager pm, String idSucursal) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaProveedor () + " WHERE idSucursal = ?");
		q.setResultClass(Sucursal.class);
		q.setParameters(idSucursal);
		return (Sucursal) q.executeUnique();
	}
	
	public List<Sucursal> darSucursales(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM" + pp.darTablaSucursal());
		q.setResultClass(Sucursal.class);
		return (List<Sucursal>) q.executeList();
	}
}
