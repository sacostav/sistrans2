package superandes.persistencia;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import oracle.net.aso.t;
import superandes.negocio.Bodega;
import superandes.negocio.Estante;

public class SQLEstante {

	private static final String SQL = PersistenciaSuperandes.SQL;

	private PersistenciaSuperandes ps;

	public SQLEstante(PersistenciaSuperandes ps)
	{
		this.ps = ps;
	}

	

	public long adicionarEstante (PersistenceManager pm,long idEstante, int nivelAbastecimiento, double volumen, double peso, String categoria, String idSucursal) 
	{
		
		Query q = pm.newQuery(SQL, "INSERT INTO " + ps.darTablaEstante() + "(idEstante, nivelAbastecimiento, volumen, peso, categoria, idSucursal) values (?,?,?,?,?,?)");
		q.setParameters( idEstante, nivelAbastecimiento, volumen, peso, categoria, idSucursal);
		return (long) q.executeUnique();
	}




	public Estante darEstantePorCategoriaySucursal (PersistenceManager pm, String categoria, String idSucursal) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + ps.darTablaEstante() + " WHERE categoria = ? and idSucursal ?");
		q.setResultClass(Estante.class);
		q.setParameters(categoria, idSucursal);
		return (Estante) q.executeUnique();
	}
	
	public List<Estante> darEstantes(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + ps.darTablaEstante());
		q.setResultClass(Estante.class);
		return q.executeResultList();
	}
	

	
}
