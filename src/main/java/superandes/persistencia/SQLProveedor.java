package superandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import java.util.List;
import java.util.LinkedList;

import superandes.negocio.Proveedor;
import superandes.negocio.Sucursal;

public class SQLProveedor {

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
	public SQLProveedor (PersistenciaSuperandes pp)
	{
		this.pp = pp;
	}

	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una Sucursal a la base de datos de SUPERANDES
	 * @param pm - El manejador de persistencia
	 * @param nit - El nit del proveedor
	 * @param nombre - El nombre del proveedor 
	 * @param calificacionCalidad - calificacion del proveedor 
	 * @param idProductos - Lista con los id de los  productos  
	 * @param idSupermercado - id del supermercado al cual pertenece la sucursal 
	 * @return El número de tuplas insertadas
	 */

	public long adicionarProveedor (PersistenceManager pm, int nit, String nombre, double calificacionCalidad ) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaProveedor () + "(NIT, nombre, calificacionCalidad, idSucursal) values (?, ?, ?)");
		q.setParameters(nit, nombre, calificacionCalidad);
		return (long) q.executeUnique();
	}


	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de un proveedor 
	 * base de datos de Superandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param NIT - El nit del proveedor 
	 * @return El objeto proveedor que tiene el nit dado
	 */
	public Proveedor darProveedorPorNit (PersistenceManager pm, int NIT) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaProveedor () + " WHERE NIT = ?");
		q.setResultClass(Proveedor.class);
		q.setParameters(NIT);
		return (Proveedor) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de Los proveedores de la 
	 * base de datos de Superandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos Proveedor
	 */
	public List<Proveedor> darProveedor (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaProveedor ());
		q.setResultClass(Proveedor.class);
		return (List<Proveedor>) q.executeList();
	}

	/**
	 * 
	 * @param pm manejador de persistencia 
	 * @param nitP nit del proveedor
	 * @return una lista con los id de los productos del proveedor
	 */

	public List<Long> darIdProductos(PersistenceManager pm, int nitP){

		Query q = pm.newQuery(SQL, "SELECT idProductos FROM "+pp.darTablaProveedor()+" WHERE NIT = ?");
		q.setParameters(nitP);
		return (List<Long>) q.executeList();

	}

	public long actualizarCalificacionCalidad(PersistenceManager pm, double cal, int nitP) {

		Query q = pm.newQuery(SQL, "SELECT calificacionCalidad FROM "+pp.darTablaProveedor()+"WHERE NIT = ?");
		q.setParameters(nitP);
		double num = (double)q.executeUnique();

		num = (num+cal)/2;

		Query s = pm.newQuery(SQL, "UPDATE "+pp.darTablaProveedor()+"SET calificacionCalidad ="+num+" WHERE NIT = ?");
		q.setParameters(nitP);

		return (long) s.executeUnique();

	}
	
	public long eliminarProveedorId(PersistenceManager pm , long id)
	{
		Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaProveedor() + "WHERE idProveedor =?");
		q.setParameters(id);
		return (long) q.executeUnique();
	}
}
