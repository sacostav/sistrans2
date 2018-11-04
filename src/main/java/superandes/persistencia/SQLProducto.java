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
		return (Long) q.executeUnique();
	}
	
	public Producto darProductoId(PersistenceManager pm, long id)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + ps.darTablaProducto() + "WHERE idProducto = ?");
		q.setResultClass(Producto.class);
		q.setParameters(id);
		return (Producto) q.executeUnique();
	}
	

	// FIXME RFC4A
	
		public List<Producto>productosPrecioEnRango(PersistenceManager pm, int inicial, int fin){

			Query q = pm.newQuery(SQL, "select * from producto where preciounitario between ? and ?;");
			q.setParameters(inicial, fin);
			return (List<Producto>) q.executeResultList();
		}

		// FIXME RFC4B

		public List<Producto>productosFechaVencimientoPosterior(PersistenceManager pm, Date date){

			Query q = pm.newQuery(SQL, "select * from producto where fechavencimiento > ?;");
			q.setParameters(date);
			return (List<Producto>) q.executeResultList();	
		}
		
		
		// FIXME RFC4C
		public List<Producto>productosVendidosPorProveedorX(PersistenceManager pm, long idProveedor){

			Query q = pm.newQuery(SQL, "select idproducto from producto  where idproveedor = ?;");
			q.setParameters(idProveedor);
			return (List<Producto>) q.executeResultList();	
		}
		
		// FIXME RFC4D

		public List<Producto>productosEnSucursalX(PersistenceManager pm, String idSucursal){

			Query q = pm.newQuery(SQL, "select idproducto from productosBodega inner join (select bodega.idbodega as idB , sucursal.idSucursal as idS from bodega inner join sucursal on bodega.idsucursal = sucursal.idsucursal) on productosBodega.idBodega = idB where idS = ?;");
			q.setParameters(idSucursal);	
			return (List<Producto>)q.executeResultList();
		}

		// FIXME RFC4E
		public List<Producto>productosPorCiudad(PersistenceManager pm, String Ciudad){

			Query q = pm.newQuery(SQL, "select idProducto from productosBodega inner join (select bodega.idBodega as idB, sucursal.ciudad as cd from sucursal inner join bodega on sucursal.idSucursal = bodega.idSucursal)	on productosBodega.idBodega = idB where cd = 'Maodao';");
			q.setParameters(Ciudad);	
			return (List<Producto>)q.executeResultList();
		}
		
		// FIXME RFC4F
		
		public List<Producto>productosPorCategoria(PersistenceManager pm, String Categoria){

			Query q = pm.newQuery(SQL, "select idProducto  from productosBodega inner join bodega on productosBodega.idBodega = bodega.idBodega where bodega.categoria = 'abarrotes';");
			q.setParameters(Categoria);	
			return (List<Producto>)q.executeResultList();
		}
		
		// FIXME RFC4G
		
		public List<Producto>productoVendidoXvecesEnRango(PersistenceManager pm, String Categoria){

			Query q = pm.newQuery(SQL, "select idProducto  from productosBodega inner join bodega on productosBodega.idBodega = bodega.idBodega where bodega.categoria = 'abarrotes';");
			q.setParameters(Categoria);	
			return (List<Producto>)q.executeResultList();
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
		return (Long) q.executeUnique();
	}

}
