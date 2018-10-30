package superandes.persistencia;

import java.util.Date;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import oracle.net.aso.t;
import superandes.negocio.Cliente;
import superandes.negocio.Producto;

public class SQLProducto {

	private static final String SQL = PersistenciaSuperandes.SQL;
	
	private PersistenciaSuperandes ps;
	
	public SQLProducto(PersistenciaSuperandes ps)
	{
		this.ps = ps;
	}
	
	public long registrarProducto(PersistenceManager pm, long idProducto, String nombre, String marca, double precioUnitario, double precioUnidadMedida, String unidadMed, double volumenEmpaque, double pesoEmpaque, String codigoBarras, Date fechaVencimiento, int nivelReorden, double precioProveedor, long idProveedor)
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + ps.darTablaProducto() + "(idProducto, nombre, marca, precioUnitario, precioUnidadMedida, unidadMed, volumenEmpaque, pesoEmpaque, codigoBarras, fechaVencimiento, nivelReorden, precioProveedor) values (?,?,?,?,?,?,?,?,?,?,?)");
		q.setParameters(idProducto,nombre, marca, precioUnitario, precioUnidadMedida, unidadMed, volumenEmpaque, pesoEmpaque, codigoBarras, fechaVencimiento, nivelReorden, precioProveedor);
		return (long) q.executeUnique();
	}
	
	public Producto darProductoId(PersistenceManager pm, long id)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + ps.darTablaProducto() + "WHERE idProducto = ?");
		q.setResultClass(Producto.class);
		q.setParameters(id);
		return (Producto) q.executeUnique();
	}
	
	public List<Producto> darProductos(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + ps.darTablaProducto());
		q.setResultClass(Producto.class);
		return q.executeResultList();
	}
	
	public long eliminarProductoId(PersistenceManager pm, long id)
	{
		Query q = pm.newQuery(SQL, "DELETE FROM " + ps.darTablaProducto() + "WHERE idProducto = ?");
		q.setParameters(id);
		return (long) q.executeUnique();
	}
}
