package superandes.negocio;

import java.util.Date;

public class Producto implements VOProducto {

	/* ***********************************************
	 * Atributos
	 *************************************************/
	
	/**
	 * Nombre del producto
	 */
	
	public static final String GRAMOS = "gramos";
	
	public static final String MILILITROS ="mililitros";
	
	private long idProducto;
		
	private String nombre;
	
	/**
	 * Marca del producto
	 */
	
	private String marca;
	
	/**
	 *  Precio del producto 
	 */
	
	private double precioUnitario;
	
	/**
	 * Precio por unidad de medida 
	 */
	
	private double precioUnidadMedida;
	
	/**
	 *  Unidad medida producto
	 */
	
	private String unidadMed;
	
	/**
	 * volumen empaque
	 */
	
	private double volumenEmpaque;
	
	/**
	 * Peso de empaque
	 */
	
	private double pesoEmpaque;
	
	/**
	 * Codigo de barras
	 */
	
	private String codigoBarras;
	
	private Date fechaVencimiento;
	
	private int nivelReorden;
	
	private double precioProveedor;
	
	private long idProveedor;
	
	

	//---------------------------- Constructor -----------------------------//

	public Producto()
	{ 
		this.idProducto = 0;
		this.nombre = "";
		this.marca = "";
		this.precioUnitario = 0;
		this.precioUnidadMedida = 0;
		this.unidadMed = "";
		this.volumenEmpaque = 0;
		this.pesoEmpaque = 0;
		this.codigoBarras = "";
	    this.fechaVencimiento = new Date(0); 
	    this.precioProveedor = 0;
		this.fechaVencimiento = new Date();
		this.nivelReorden = 0;
		this.idProveedor = 0;
	}
	
	public Producto(long idProducto, String nombre, String marca, double precioUnitario, double precioUnidad, String unidad, double volumen, double peso, String codigo,  Date fechaVencimiento2, int nivelReorden, double precioProveedor, long idProveedor)
	{
		this.idProducto = idProducto;
		this.nombre = nombre;
		this.marca = marca;
		this.precioUnitario = precioUnitario;
		this.precioUnidadMedida = precioUnidad;
		this.unidadMed = unidad;
		this.volumenEmpaque = volumen;
		this.pesoEmpaque = peso;
		this.codigoBarras = codigo;
		this.fechaVencimiento = fechaVencimiento2;
		this.nivelReorden = nivelReorden;
		this.precioProveedor = precioProveedor;
		this.idProveedor = idProveedor;
	}

	//--------------------------------- Métodos -------------------------------------//
	
	public long getIdProducto()
	{
		return idProducto;
	}
	
	public void setIdProducto(long idProducto)
	{
		this.idProducto = idProducto;
	}
	
	
	public String getNombre() {
		// TODO Auto-generated method stub
		return nombre;
	}
	
	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}

	
	public String getMarca() {
		// TODO Auto-generated method stub
		return marca;
	}

	public void setMarca(String marca)
	{
		this.marca = marca;
	}
	
	
	public double getPrecioUnitario() {
		// TODO Auto-generated method stub
		return precioUnitario;
	}

	public void setPrecioUnitario(double precio)
	{
		this.precioUnitario = precio;
	}
	
	
	public double getPrecioUnidadMedida() {
		// TODO Auto-generated method stub
		return precioUnidadMedida;
	}

	public void setPrecioUnidad(double precio)
	{
		this.precioUnidadMedida = precio;
	}
	
	
	public double getVolumenEmpaque() {
		// TODO Auto-generated method stub
		return volumenEmpaque;
	}

	public void setVolumen(double volumen)
	{
		this.volumenEmpaque = volumen;
	}
	
	
	public double getPesoEmpaque() {
		// TODO Auto-generated method stub
		return pesoEmpaque;
	}
	
	public void setPeso(double peso)
	{
		this.pesoEmpaque = peso;
	}

	
	public String getCodigoBarras() {
		// TODO Auto-generated method stub
		return codigoBarras;
	}

	public void setCodigo(String codigo)
	{
		this.codigoBarras = codigo;
	}
	
	
	public Date getFechaVencimiento() {
		// TODO Auto-generated method stub
		return fechaVencimiento;
	}
	
	
	public void setFechaVenciminto(Date fecha)
	{
		this.fechaVencimiento = fecha;
	}
	
	public int getNivelReorden()
	{
		return nivelReorden;
	}
	
	public void setNivelReorden(int nivel)
	{
		this.nivelReorden = nivel;
	}
	
	
	public double getPrecioProveedor() {
		return precioProveedor;
	}
	
	public void setPrecioProveedor(double prec) {
		precioProveedor = prec;
	}
	
	public String toString()
	{
		return "Producto [ idProducto =" + idProducto + "nombre =" + nombre + "marca" + "precio unitario" + precioUnitario + "precio unidad" + precioUnidadMedida + "unidad medida" + unidadMed + "volumen del empaque" + volumenEmpaque + "peso empaque" + pesoEmpaque
				+ "codigo de barras" + codigoBarras + "fecha vencimiento " + fechaVencimiento + "nivel de reorden " + nivelReorden +  "]";
	}

	

	
	
	
	
	
	
	
	
	
	
	
	
	
}

