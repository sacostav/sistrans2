package superandes.persistencia;

import java.util.Date;
import java.util.LinkedList;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import superandes.negocio.Cliente;
import superandes.negocio.Promocion;

public class SQLPromocion {

		
		private static final String SQL = PersistenciaSuperandes.SQL;
		
		private PersistenciaSuperandes ps;
		
		public SQLPromocion(PersistenciaSuperandes ps)
		{
			this.ps = ps;
		}
		
		public long registrarPromocion(PersistenceManager pm, long idPromocion, String tipoPromocion, Date fechaFin, java.util.Date fechaInicio)
		{
			Query q = pm.newQuery(SQL, "INSERT INTO " + ps.darTablaPromocion() + "(idPromocion, tipoPromocion, fechaFin, fechaInicio) values (?,?,?,?)");
			q.setParameters(idPromocion, tipoPromocion, fechaFin, fechaInicio);
			return (long) q.executeUnique();
		}
		
		
		public Promocion darPromocionPorId(PersistenceManager pm, long id)
		{
			Query q = pm.newQuery(SQL, "SELECT * FROM " + ps.darTablaPromocion() + "WHERE id = ?");
			q.setResultClass(Promocion.class);
			q.setParameters(id);
			return (Promocion) q.executeUnique();
		}

		
		
}
