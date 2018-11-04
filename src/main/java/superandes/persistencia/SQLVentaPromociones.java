package superandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLVentaPromociones {


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
	public SQLVentaPromociones (PersistenciaSuperandes pp)
	{
		this.pp = pp;
	}

	public long adicionarVentaPromociones (PersistenceManager pm, long idPromocion, long idVenta) 

	{

		Query q = pm.newQuery(SQL, " INSERT INTO VENTAPROMOCIONES(IDPROMOCION, IDVENTA) values (?, ?)");
		q.setParameters(idPromocion, idVenta);
		return (Long) q.executeUnique();
	}

	
	// FIXME RFC2
	public List<Object[]> Top20PromocionesPopulares(PersistenceManager pm){
		
		Query q = pm.newQuery(SQL, "create view req2 as SELECT  count(*) as num, idPromocion FROM ventasPromociones  group by idPromocion order by num desc;  select * from req2 where rownum <=21;");
	    return (List<Object[]>)q.executeList();
	}
	
}
