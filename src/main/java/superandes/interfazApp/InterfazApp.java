package superandes.interfazApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import superandes.negocio.Superandes;
import superandes.negocio.VOBodega;
import superandes.negocio.VOCliente;
import superandes.negocio.VOEstante;
import superandes.negocio.VOPedido;
import superandes.negocio.VOProducto;
import superandes.negocio.VOPromocion;
import superandes.negocio.VOProveedor;
import superandes.negocio.VOSucursal;
import superandes.negocio.VOVenta;


public class InterfazApp extends JFrame implements ActionListener{

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecuci�n
	 */
	private static Logger log = Logger.getLogger(InterfazApp.class.getName());
	
	/**
	 * Ruta al archivo de configuraci�n de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp.json"; 
	
	/**
	 * Ruta al archivo de configuraci�n de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
    /**
     * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
     */
    private JsonObject tableConfig;
    
    /**
     * Asociaci�n a la clase principal del negocio.
     */
    private Superandes superandes;
    
	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
    /**
     * Objeto JSON con la configuraci�n de interfaz de la app.
     */
    private JsonObject guiConfig;
    
    /**
     * Panel de despliegue de interacci�n para los requerimientos
     */
    private PanelDatos panelDatos;
    
    /**
     * Men� de la aplicaci�n
     */
    private JMenuBar menuBar;

	/* ****************************************************************
	 * 			M�todos
	 *****************************************************************/
    /**
     * Construye la ventana principal de la aplicaci�n. <br>
     * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
     */
    public InterfazApp( )
    {
        // Carga la configuraci�n de la interfaz desde un archivo JSON
        guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);
        
        // Configura la apariencia del frame que contiene la interfaz gr�fica
        configurarFrame ( );
        if (guiConfig != null) 	   
        {
     	   crearMenu( guiConfig.getAsJsonArray("menuBar") );
        }
        
        tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
        superandes = new Superandes (tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
	/* ****************************************************************
	 * 			M�todos de configuraci�n de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuraci�n para la aplicaci�, a partir de un archivo JSON o con valores por defecto si hay errores.
     * @param tipo - El tipo de configuraci�n deseada
     * @param archConfig - Archivo Json que contiene la configuraci�n
     * @return Un objeto JSON con la configuraci�n del tipo especificado
     * 			NULL si hay un error en el archivo.
     */
    private JsonObject openConfig (String tipo, String archConfig)
    {
    	JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontr� un archivo de configuraci�n v�lido: " + tipo);
		} 
		catch (Exception e)
		{
//			e.printStackTrace ();
			log.info ("NO se encontr� un archivo de configuraci�n v�lido");			
			JOptionPane.showMessageDialog(null, "No se encontr� un archivo de configuraci�n de interfaz v�lido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
        return config;
    }
    
    /**
     * M�todo para configurar el frame principal de la aplicaci�n
     */
    private void configurarFrame(  )
    {
    	int alto = 0;
    	int ancho = 0;
    	String titulo = "";	
    	
    	if ( guiConfig == null )
    	{
    		log.info ( "Se aplica configuraci�n por defecto" );			
			titulo = "Parranderos APP Default";
			alto = 300;
			ancho = 500;
    	}
    	else
    	{
			log.info ( "Se aplica configuraci�n indicada en el archivo de configuraci�n" );
    		titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
    	}
    	
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setLocation (50,50);
        setResizable( true );
        setBackground( Color.WHITE );

        setTitle( titulo );
		setSize ( ancho, alto);        
    }

    /**
     * M�todo para crear el men� de la aplicaci�n con base em el objeto JSON le�do
     * Genera una barra de men� y los men�s con sus respectivas opciones
     * @param jsonMenu - Arreglo Json con los men�s deseados
     */
    private void crearMenu(  JsonArray jsonMenu )
    {    	
    	// Creaci�n de la barra de men�s
        menuBar = new JMenuBar();       
        for (JsonElement men : jsonMenu)
        {
        	// Creaci�n de cada uno de los men�s
        	JsonObject jom = men.getAsJsonObject(); 

        	String menuTitle = jom.get("menuTitle").getAsString();        	
        	JsonArray opciones = jom.getAsJsonArray("options");
        	
        	JMenu menu = new JMenu( menuTitle);
        	
        	for (JsonElement op : opciones)
        	{       	
        		// Creaci�n de cada una de las opciones del men�
        		JsonObject jo = op.getAsJsonObject(); 
        		String lb =   jo.get("label").getAsString();
        		String event = jo.get("event").getAsString();
        		
        		JMenuItem mItem = new JMenuItem( lb );
        		mItem.addActionListener( this );
        		mItem.setActionCommand(event);
        		
        		menu.add(mItem);
        	}       
        	menuBar.add( menu );
        }        
        setJMenuBar ( menuBar );	
    }
    
	/* ****************************************************************
	 * 			CRUD de Bodega
	 *****************************************************************/
    /**
     * Adiciona un tipo de bebida con la informaci�n dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no exist�a
     */
    public void adicionarBodega( )
    {
    	try 
    	{
    		long id = Long.parseLong(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		double volumen = Long.parseLong(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		double peso = Long.parseLong(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		String categoria = JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE);
    		String idSucursal = JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE);
    		int nivel = Integer.parseInt(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		
    		if (id != 0)
    		{
        		VOBodega tb = superandes.adicionarBodega(id, peso, volumen, categoria, idSucursal, nivel);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear una bodega con id: " + id);
        		}
        		String resultado = "En adicionar Bodega\n\n";
        		resultado += "Bodega adicionado exitosamente: " + tb;
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicaci�n
     */
    public void listarBodega( )
    {
    	try 
    	{
			List <VOBodega> lista = superandes.darVOBodega();

			String resultado = "En listarTipoBebida";
			resultado +=  "\n" + listarBodega (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operaci�n terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el tipo de bebida con el identificador dado po el usuario
     * Cuando dicho tipo de bebida no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarTipoBebidaPorId( )
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id del tipo de bedida?", "Borrar tipo de bebida por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idTipoStr != null)
    		{
    			long idTipo = Long.valueOf (idTipoStr);
    			long tbEliminados = superandes.eliminarBodega (idTipo);

    			String resultado = "En eliminar TipoBebida\n\n";
    			resultado += tbEliminados + " Tipos de bebida eliminados\n";
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Busca el tipo de bebida con el nombre indicado por el usuario y lo muestra en el panel de datos
     */
    public void buscarTipoBebidaPorId( )
    {
    	try 
    	{
    		long id = Long.parseLong(JOptionPane.showInputDialog (this, "Id de la bodega?", "Buscar bodega por id", JOptionPane.QUESTION_MESSAGE));
    		if (id !=0)
    		{
    			VOBodega tipoBebida = superandes.darBodegaPorId(id);
    			String resultado = "En buscar Tipo Bebida por nombre\n\n";
    			if (tipoBebida != null)
    			{
        			resultado += "El tipo de bebida es: " + tipoBebida;
    			}
    			else
    			{
        			resultado += "Un tipo de bebida con nombre: " + id + " NO EXISTE\n";    				
    			}
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /* ****************************************************************
	 * 			CRUD de Cliente
	 *****************************************************************/
    /**
     * Adiciona un tipo de bebida con la informaci�n dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no exist�a
     */
    public void adicionarCliente( )
    {
    	try 
    	{
    		long id = Long.parseLong(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		int documento = Integer.parseInt(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		int nit = Integer.parseInt(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		String nombre = JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE);
    		String correo = JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE);
    		String direccion = JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE);
    		String tipo = JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE);

    		
    		if (id != 0)
    		{
        		VOCliente tb = superandes.registrarCliente(documento, nit, nombre, direccion, correo, tipo);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un cliente con id: " + id);
        		}
        		String resultado = "En adicionar Cliente\n\n";
        		resultado += "Clienete adicionado exitosamente: " + tb;
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicaci�n
     */
    public void listarCliente( )
    {
    	try 
    	{
			List <VOCliente> lista = superandes.darVOCliente();

			String resultado = "En listarTipoBebida";
			resultado +=  "\n" + listarCliente (lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operaci�n terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el tipo de bebida con el identificador dado po el usuario
     * Cuando dicho tipo de bebida no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarClientePorId( )
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id del cliente?", "Borrar cliente por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idTipoStr != null)
    		{
    			long idTipo = Long.valueOf (idTipoStr);
    			long tbEliminados = superandes.eliminarCliente (idTipo);

    			String resultado = "En eliminar TipoBebida\n\n";
    			resultado += tbEliminados + " Tipos de bebida eliminados\n";
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Busca el tipo de bebida con el nombre indicado por el usuario y lo muestra en el panel de datos
     */
    public void buscarClientePorId( )
    {
    	try 
    	{
    		long id = Long.parseLong(JOptionPane.showInputDialog (this, "Id del cliente?", "Buscar cliente por id", JOptionPane.QUESTION_MESSAGE));
    		if (id !=0)
    		{
    			VOCliente tipoBebida = superandes.darClientePorId(id);
    			String resultado = "En buscar cliente por id\n\n";
    			if (tipoBebida != null)
    			{
        			resultado += "El cliente es: " + tipoBebida;
    			}
    			else
    			{
        			resultado += "Un cliente con id: " + id + " NO EXISTE\n";    				
    			}
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    /* ****************************************************************
	 * 			CRUD de Estante
	 *****************************************************************/
    /**
     * Adiciona un tipo de bebida con la informaci�n dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no exist�a
     */
    public void adicionarEstante( )
    {
    	try 
    	{
    		long id = Long.parseLong(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		int nivel = Integer.parseInt(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		double volumen = Double.parseDouble(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		double peso = Double.parseDouble(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		String categoria = JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE);
    		String idSucursal = JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE);
    		
    		if (id != 0)
    		{
        		VOEstante tb = superandes.registrarEstante(nivel, volumen, peso, categoria, idSucursal);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un estante con id: " + id);
        		}
        		String resultado = "En adicionar estante\n\n";
        		resultado += "Estante adicionado exitosamente: " + tb;
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicaci�n
     */
    public void listarEstante( )
    {
    	try 
    	{
			List <VOEstante> lista = superandes.darVOEstante();

			String resultado = "En listarEstante";
			resultado +=  "\n" + listarEstante(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operaci�n terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el tipo de bebida con el identificador dado po el usuario
     * Cuando dicho tipo de bebida no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarEstantePorId( )
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id del estante?", "Borrar estamte por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idTipoStr != null)
    		{
    			long idTipo = Long.valueOf (idTipoStr);
    			long tbEliminados = superandes.eliminarEstante (idTipo);

    			String resultado = "En eliminar Estante\n\n";
    			resultado += tbEliminados + " Estantes eliminados\n";
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Busca el tipo de bebida con el nombre indicado por el usuario y lo muestra en el panel de datos
     */
    public void buscarEstantePorId( )
    {
    	try 
    	{
    		long id = Long.parseLong(JOptionPane.showInputDialog (this, "Id del estante?", "Buscar estante por id", JOptionPane.QUESTION_MESSAGE));
    		if (id !=0)
    		{
    			VOEstante tipoBebida = superandes.darEstantePorId(id);
    			String resultado = "En buscar estante por id\n\n";
    			if (tipoBebida != null)
    			{
        			resultado += "El estante es: " + tipoBebida;
    			}
    			else
    			{
        			resultado += "Un estante con id: " + id + " NO EXISTE\n";    				
    			}
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    /* ****************************************************************
	 * 			CRUD de Pedido
	 *****************************************************************/
    /**
     * Adiciona un tipo de bebida con la informaci�n dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no exist�a
     */
    public void adicionarPedido( )
    {
    	try 
    	{
    		long id = Long.parseLong(JOptionPane.showInputDialog (this, "Id de la pedido ?", "Adicionar pedido", JOptionPane.QUESTION_MESSAGE));
    		Date fechaPedido = new Date(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		Date fechaLlegada = new Date(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		String estado = (JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		String idSucursal = JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE);
    		long idProveedor = Long.parseLong(JOptionPane.showInputDialog (this, "Id de la pedido ?", "Adicionar pedido", JOptionPane.QUESTION_MESSAGE));
    		
    		if (id != 0)
    		{
        		VOPedido tb = superandes.registrarPedido(fechaPedido, fechaLlegada, idSucursal, idProveedor, estado);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un pedido con id: " + id);
        		}
        		String resultado = "En adicionar pedido\n\n";
        		resultado += "Pedido adicionado exitosamente: " + tb;
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicaci�n
     */
    public void listarPedido( )
    {
    	try 
    	{
			List <VOPedido> lista = superandes.darVOPedido();

			String resultado = "En listarEstante";
			resultado +=  "\n" + listarPedido(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operaci�n terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el tipo de bebida con el identificador dado po el usuario
     * Cuando dicho tipo de bebida no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarPedidoPorId( )
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id del pedido?", "Borrar pedido por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idTipoStr != null)
    		{
    			long idTipo = Long.valueOf (idTipoStr);
    			long tbEliminados = superandes.eliminarPedido (idTipo);

    			String resultado = "En eliminar Pedido\n\n";
    			resultado += tbEliminados + " Pedidos eliminados\n";
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Busca el tipo de bebida con el nombre indicado por el usuario y lo muestra en el panel de datos
     */
    public void buscarPedidoPorId( )
    {
    	try 
    	{
    		long id = Long.parseLong(JOptionPane.showInputDialog (this, "Id del pedido?", "Buscar pedido por id", JOptionPane.QUESTION_MESSAGE));
    		if (id !=0)
    		{
    			VOPedido tipoBebida = superandes.darPedidoPorId(id);
    			String resultado = "En buscar pedido por id\n\n";
    			if (tipoBebida != null)
    			{
        			resultado += "El pedido es: " + tipoBebida;
    			}
    			else
    			{
        			resultado += "Un pedido con id: " + id + " NO EXISTE\n";    				
    			}
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /* ****************************************************************
	 * 			CRUD de Producto
	 *****************************************************************/
    /**
     * Adiciona un tipo de bebida con la informaci�n dada por el usuario
     * Se crea una nueva tupla de tipoBebida en la base de datos, si un tipo de bebida con ese nombre no exist�a
     */
    public void adicionarProducto( )
    {
    	try 
    	{
    		long id = Long.parseLong(JOptionPane.showInputDialog (this, "Id de la pedido ?", "Adicionar pedido", JOptionPane.QUESTION_MESSAGE));
    		String nombre = (JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		String marca = (JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		double precioUnitario = Double.parseDouble(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		double precioUnidad = Double.parseDouble(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		String unidadMed = JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE);
    		double volumen = Double.parseDouble(JOptionPane.showInputDialog (this, "Id de la pedido ?", "Adicionar pedido", JOptionPane.QUESTION_MESSAGE));
    		double peso = Double.parseDouble(JOptionPane.showInputDialog (this, "Id de la pedido ?", "Adicionar pedido", JOptionPane.QUESTION_MESSAGE));
    		String codigo = JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE);
    		Date fecha = new Date(JOptionPane.showInputDialog (this, "Id de la bodega ?", "Adicionar bodega", JOptionPane.QUESTION_MESSAGE));
    		int nivel = Integer.parseInt(JOptionPane.showInputDialog (this, "Id de la pedido ?", "Adicionar pedido", JOptionPane.QUESTION_MESSAGE));
    		double precioProveedor = Double.parseDouble(JOptionPane.showInputDialog (this, "Id de la pedido ?", "Adicionar pedido", JOptionPane.QUESTION_MESSAGE));
    		long idProveedor = Long.parseLong(JOptionPane.showInputDialog (this, "Id de la pedido ?", "Adicionar pedido", JOptionPane.QUESTION_MESSAGE));

    		
    		if (id != 0)
    		{
        		VOPedido tb = superandes.registrarPedido(nombre, marca, precioUnidad, peso, precioUnitario);
        		if (tb == null)
        		{
        			throw new Exception ("No se pudo crear un pedido con id: " + id);
        		}
        		String resultado = "En adicionar pedido\n\n";
        		resultado += "Pedido adicionado exitosamente: " + tb;
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Consulta en la base de datos los tipos de bebida existentes y los muestra en el panel de datos de la aplicaci�n
     */
    public void listarPedido( )
    {
    	try 
    	{
			List <VOPedido> lista = superandes.darVOPedido();

			String resultado = "En listarEstante";
			resultado +=  "\n" + listarPedido(lista);
			panelDatos.actualizarInterfaz(resultado);
			resultado += "\n Operaci�n terminada";
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Borra de la base de datos el tipo de bebida con el identificador dado po el usuario
     * Cuando dicho tipo de bebida no existe, se indica que se borraron 0 registros de la base de datos
     */
    public void eliminarPedidoPorId( )
    {
    	try 
    	{
    		String idTipoStr = JOptionPane.showInputDialog (this, "Id del pedido?", "Borrar pedido por Id", JOptionPane.QUESTION_MESSAGE);
    		if (idTipoStr != null)
    		{
    			long idTipo = Long.valueOf (idTipoStr);
    			long tbEliminados = superandes.eliminarPedido (idTipo);

    			String resultado = "En eliminar Pedido\n\n";
    			resultado += tbEliminados + " Pedidos eliminados\n";
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

    /**
     * Busca el tipo de bebida con el nombre indicado por el usuario y lo muestra en el panel de datos
     */
    public void buscarPedidoPorId( )
    {
    	try 
    	{
    		long id = Long.parseLong(JOptionPane.showInputDialog (this, "Id del pedido?", "Buscar pedido por id", JOptionPane.QUESTION_MESSAGE));
    		if (id !=0)
    		{
    			VOPedido tipoBebida = superandes.darPedidoPorId(id);
    			String resultado = "En buscar pedido por id\n\n";
    			if (tipoBebida != null)
    			{
        			resultado += "El pedido es: " + tipoBebida;
    			}
    			else
    			{
        			resultado += "Un pedido con id: " + id + " NO EXISTE\n";    				
    			}
    			resultado += "\n Operaci�n terminada";
    			panelDatos.actualizarInterfaz(resultado);
    		}
    		else
    		{
    			panelDatos.actualizarInterfaz("Operaci�n cancelada por el usuario");
    		}
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
    
	/* ****************************************************************
	 * 			M�todos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de Parranderos
	 */
	public void mostrarLogParranderos ()
	{
		mostrarArchivo ("parranderos.log");
	}
	
	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}
	
	/**
	 * Limpia el contenido del log de parranderos
	 * Muestra en el panel de datos la traza de la ejecuci�n
	 */
	public void limpiarLogParranderos ()
	{
		// Ejecuci�n de la operaci�n y recolecci�n de los resultados
		boolean resp = limpiarArchivo ("parranderos.log");

		// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
		String resultado = "\n\n************ Limpiando el log de parranderos ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecuci�n
	 */
	public void limpiarLogDatanucleus ()
	{
		// Ejecuci�n de la operaci�n y recolecci�n de los resultados
		boolean resp = limpiarArchivo ("datanucleus.log");

		// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}
	
	/**
	 * Limpia todas las tuplas de todas las tablas de la base de datos de parranderos
	 * Muestra en el panel de datos el n�mero de tuplas eliminadas de cada tabla
	 */
	public void limpiarBD ()
	{
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			long eliminados [] = superandes.limpiarSuperandes();
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados [0] + " Gustan eliminados\n";
			resultado += eliminados [1] + " Sirven eliminados\n";
			resultado += eliminados [2] + " Visitan eliminados\n";
			resultado += eliminados [3] + " Bebidas eliminadas\n";
			resultado += eliminados [4] + " Tipos de bebida eliminados\n";
			resultado += eliminados [5] + " Bebedores eliminados\n";
			resultado += eliminados [6] + " Bares eliminados\n";
			resultado += "\nLimpieza terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	
	/**
	 * Muestra la presentaci�n general del proyecto
	 */
	public void mostrarPresentacionGeneral ()
	{
		mostrarArchivo ("data/00-ST-ParranderosJDO.pdf");
	}
	
	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Parranderos.pdf");
	}
	
	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Parranderos.pdf");
	}
	
	/**
	 * Muestra el script de creaci�n de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/EsquemaParranderos.sql");
	}
	
	/**
	 * Muestra la arquitectura de referencia para Parranderos
	 */
	public void mostrarArqRef ()
	{
		mostrarArchivo ("data/ArquitecturaReferencia.pdf");
	}
	
	/**
	 * Muestra la documentaci�n Javadoc del proyectp
	 */
	public void mostrarJavadoc ()
	{
		mostrarArchivo ("doc/index.html");
	}
	
	/**
     * Muestra la informaci�n acerca del desarrollo de esta apicaci�n
     */
    public void acercaDe ()
    {
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogot�	- Colombia)\n";
		resultado += " * Departamento	de	Ingenier�a	de	Sistemas	y	Computaci�n\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versi�n 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Parranderos Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Germ�n Bravo\n";
		resultado += " * Julio de 2018\n";
		resultado += " * \n";
		resultado += " * Revisado por: Claudia Jim�nez, Christian Ariza\n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
    }
    

	/* ****************************************************************
	 * 			M�todos privados para la presentaci�n de resultados y otras operaciones
	 *****************************************************************/
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una l�nea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una l�ea para cada tipo de bebida recibido
     */
    private String listarBodega(List<VOBodega> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOBodega tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una l�nea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una l�ea para cada tipo de bebida recibido
     */
    private String listarCliente(List<VOCliente> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOCliente tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una l�nea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una l�ea para cada tipo de bebida recibido
     */
    private String listarEstante(List<VOEstante> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOEstante tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una l�nea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una l�ea para cada tipo de bebida recibido
     */
    private String listarPedido(List<VOPedido> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOPedido tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una l�nea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una l�ea para cada tipo de bebida recibido
     */
    private String listarProducto(List<VOProducto> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOProducto tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una l�nea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una l�ea para cada tipo de bebida recibido
     */
    private String listarPromocion(List<VOPromocion> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOPromocion tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una l�nea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una l�ea para cada tipo de bebida recibido
     */
    private String listarProveedor(List<VOProveedor> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOProveedor tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una l�nea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una l�ea para cada tipo de bebida recibido
     */
    private String listarSucursal(List<VOSucursal> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOSucursal tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de los tipos de bebida recibida: una l�nea por cada tipo de bebida
     * @param lista - La lista con los tipos de bebida
     * @return La cadena con una l�ea para cada tipo de bebida recibido
     */
    private String listarVenta(List<VOVenta> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOVenta tb : lista)
        {
        	resp += i++ + ". " + tb.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la descripci�n de la excepcion e, haciendo �nfasis en las excepcionsde JDO
     * @param e - La excepci�n recibida
     * @return La descripci�n de la excepci�n, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
     */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicaci�n
	 * @param e - La excepci�n generada
	 * @return La cadena con la informaci�n de la excepci�n y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecuci�n\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y parranderos.log para m�s detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como par�metro con la aplicaci�n por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/* ****************************************************************
	 * 			M�todos de la Interacci�n
	 *****************************************************************/
    /**
     * M�todo para la ejecuci�n de los eventos que enlazan el men� con los m�todos de negocio
     * Invoca al m�todo correspondiente seg�n el evento recibido
     * @param pEvento - El evento del usuario
     */
    @Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
        try 
        {
			Method req = InterfazApp.class.getMethod ( evento );			
			req.invoke ( this );
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		} 
	}
    
	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
    /**
     * Este m�todo ejecuta la aplicaci�n, creando una nueva interfaz
     * @param args Arreglo de argumentos que se recibe por l�nea de comandos
     */
    public static void main( String[] args )
    {
        try
        {
        	
            // Unifica la interfaz para Mac y para Windows.
            UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
            InterfazApp interfaz = new InterfazApp( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
