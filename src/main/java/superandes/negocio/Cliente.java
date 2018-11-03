package superandes.negocio;

public class Cliente implements VOCliente {

	public final static String PERSONA_NATURAL = "Persona natural";
	
	public final static String EMPRESA = "Empresa";
	
	
	/* ******************************************
	 * Atributos
	 ********************************************/
	/**
	 * Identicación cliente
	 */
	private long id;
	
	/**
	 * Numero documento identificacion cliente 
	 */
	private int documentoIdentificacion;
	
	/**
	 * Nit del cliente si es una empresa 
	 */
	private int NIT;
	
	/**
	 * Nombre del cliente 
	 */
	private String nombre;
	
	/**
	 * Correo del cliente 
	 */
	private String correo;
	
	/**
	 * Direccion del cliente si es una empresa 
	 */
	private String direccion;
	
	/**
	 * Tipo del clinte 
	 */
	
	private String tipoCliente;
	
		
	//---------------------------- Constructor -----------------------------//

	public Cliente() {
		this.id = 0;
		this.documentoIdentificacion = 0;
		this.NIT = 0;
		this.nombre = "";
		this.correo = "";
		this.direccion = "";
		this.tipoCliente = "";
	}

	public Cliente(long id, int documentoIdentificacion,int NIT, String nombre, String correo, String direccion,String tipoCliente)
	{
		this.id = id;
		this.documentoIdentificacion = documentoIdentificacion;
		this.NIT = NIT;
		this.nombre = nombre;
		this.correo = correo;
		this.direccion = direccion;
		this.tipoCliente = tipoCliente;
	}
	
	//--------------------------------- Métodos -------------------------------------//
	
	public int getDocumento() {
		// TODO Auto-generated method stub
		return documentoIdentificacion;
	}

	public void setDocumento(int id)
	{
		this.documentoIdentificacion = id;
	}
	
	
	public int getNIT() {
		// TODO Auto-generated method stub
		return NIT;
	}

	public void setNit(int nit)
	{
		this.NIT = nit;
	}
	
	
	public String getNombre() {
		// TODO Auto-generated method stub
		return nombre;
	}

	public void setNombre(String nombre)
	{
		this.nombre = nombre;
	}
	
	
	public String getCorreo() {
		// TODO Auto-generated method stub
		return correo;
	}

	public void setCorreo(String correo)
	{
		this.correo = correo;
	}
	
	
	public String getDireccion() {
		// TODO Auto-generated method stub
		return direccion;
	}

	public void getDireccion(String direccion)
	{
		this.direccion = direccion;
	}
	
	
	public String getTipoCliente() {
		// TODO Auto-generated method stub
		return tipoCliente;
	}
	

	public void setTipoCliente(String tipoCliente)
	{
		this.tipoCliente = tipoCliente;
	}

	
	public long getId() {
		// TODO Auto-generated method stub
		return id;
	}
	
	public void setId(long id)
	{
		this.id = id;
	}

	
	public String toString()
	{
		return "Cliente [ id=" + documentoIdentificacion + "nombre" + nombre + "correo" + correo + "dirección" + direccion + "tipo cliente" + tipoCliente + "]";
	}

}
	
