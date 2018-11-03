package superandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superandes.negocio.Bodega;
import superandes.negocio.Carro;

public class SQLCarro {
	
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
	public SQLCarro (PersistenciaSuperandes pp)
	{
		this.pp = pp;
	}

	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una BODEGA a la base de datos de SUPERANDES
	 * @param pm - El manejador de persistencia
	 * @param idBodega - El identificador de la bodega
	 * @param categoria - categoria producto 
	 * @param peso - El peso maximo de capacidad en la bodega 
	 * @param volumen - volumen maximo en la bodega
	 * @param idSucursal - identificador de la sucursal a la cual pertenece la bodega 
	 * @return El número de tuplas insertadas
	 */

	public long adicionarCarro (PersistenceManager pm, long idCarro, long idSucursal) 
	
	{
		
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCarro() + "(idCarro, idSucursal) values (?, ?)");
		q.setParameters(idCarro, idSucursal);
		return (Long) q.executeUnique();
	}



	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de una bodepa  
	 * base de datos de Superandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param idBodega - El identificador de la bodega
	 * @return El objeto Bodega que tiene el identificador dado
	 */
	public Carro darCarroPorId (PersistenceManager pm, long idCarro) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCarro () + " WHERE idCarro = ?");
		q.setResultClass(Carro.class);
		q.setParameters(idCarro);
		return (Carro) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de Las bodegas de la 
	 * base de datos de Superandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos Bar
	 */
	public List<Carro> darCarros (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCarro ());
		q.setResultClass(Carro.class);
		return (List<Carro>) q.executeList();
	}
	
	public List<String> cantidadProductosEnCarro(PersistenceManager pm) {
		
		Query q = pm.newQuery(SQL, "select  count(nombre), producto.codigobarras from "+pp.darTablaProducto()+ " inner join "+pp.darTablaProductosCarro()+" on producto.idproducto = productos_carro.idproducto group by producto.codigobarras");
		return (List<String>)q.executeList();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar BEBEDORES de la base de datos de Parranderos, por su nombre
	 * @param pm - El manejador de persistencia
	 * @param nombre - El nombre del bebedor
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarCarro (PersistenceManager pm, long id)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCarro() + " WHERE idCarro = ?");
        q.setParameters(id);
        return (Long) q.executeUnique();            
	} 

}
