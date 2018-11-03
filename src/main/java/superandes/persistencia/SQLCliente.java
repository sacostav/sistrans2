package superandes.persistencia;

import java.util.LinkedList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import oracle.net.aso.t;
import superandes.negocio.Bodega;
import superandes.negocio.Cliente;
import superandes.negocio.Venta;

public class SQLCliente {

	private static final String SQL = PersistenciaSuperandes.SQL;
	
	private  PersistenciaSuperandes ps;
	
	public SQLCliente(PersistenciaSuperandes ps)
	{
		this.ps = ps;
	}
	
	public long registrarCliente(PersistenceManager pm,long id, int documentoIdentificacion,int NIT, String nombre, String correo, String direccion,String tipoCliente)
	{
	
		Query q = pm.newQuery(SQL, "INSERT INTO " + ps.darTablaCliente() + "(id, documentoIdentificacion, NIT, nombre, correo, direccion, tipoCliente) values (?,?,?,?,?,?,?)");
		q.setParameters(id, documentoIdentificacion,NIT, nombre, correo, direccion, tipoCliente);
		return (Long) q.executeUnique();
	}
	
	
	
	public Cliente darClienteId(PersistenceManager pm, long id)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + ps.darTablaCliente() + "WHERE id = ?");
		q.setResultClass(Cliente.class);
		q.setParameters(id);
		return (Cliente) q.executeUnique();
	}
	

	
	
	public List<Cliente> darClientes(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + ps.darTablaCliente());
		q.setResultClass(Cliente.class);
		return (List<Cliente>) q.executeList();
	}
	
	public long eliminarClienteId(PersistenceManager pm, long id)
	{
		Query q = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaCliente() + "WHERE idCliente =?");
		q.setParameters(id);
		return (Long) q.executeUnique();
	}
}
