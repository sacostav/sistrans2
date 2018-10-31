package superandes.interfazDemo;

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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

public class InterfazDemo extends JFrame implements ActionListener{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecuci�n
	 */
	private static Logger log = Logger.getLogger(InterfazDemo.class.getName());
	
	/**
	 * Ruta al archivo de configuraci�n de la interfaz
	 */
	private final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigDemo.json"; 
	
	/**
	 * Ruta al archivo de configuraci�n de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD.json"; 
	
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
    public InterfazDemo( )
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
        superandes = new Superandes(tableConfig);
        
    	String path = guiConfig.get("bannerPath").getAsString();
        panelDatos = new PanelDatos ( );

        setLayout (new BorderLayout());
        add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
        add( panelDatos, BorderLayout.CENTER );        
    }
    
	/* ****************************************************************
	 * 			M�todos para la configuraci�n de la interfaz
	 *****************************************************************/
    /**
     * Lee datos de configuraci�n para la aplicaci�n, a partir de un archivo JSON o con valores por defecto si hay errores.
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
			JOptionPane.showMessageDialog(null, "No se encontr� un archivo de configuraci�n de interfaz v�lido: " + tipo, "Superandes App", JOptionPane.ERROR_MESSAGE);
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
			titulo = "Superandes APP Default";
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
	 * 			Demos de TipoBebida
	 *****************************************************************/
    /**
     * Demostraci�n de creaci�n, consulta y borrado de Tipos de Bebida
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoBodega( )
    {
    	try 
    	{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			double peso = 68;
			double volumen = 860;
			long id = 668;
			String categoria = "Congelados";
			double nivel = 76;
			String idSucursal = "esto";
			boolean errorBodega = false;
			VOBodega bodega = superandes.adicionarBodega(id, peso, volumen, categoria, idSucursal, nivel);
			if (bodega == null)
			{
				bodega = superandes.darBodegaPorId(id);
				errorBodega = true;
			}
			List <VOBodega> lista = superandes.darVOBodega();
			long tbEliminados = superandes.eliminarBodega(bodega.getIdBodega());
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de TipoBebida\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorBodega)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado += "Adicionado el tipo de bebida con nombre: " + id + "\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado +=  "\n" + listarBodega (lista);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += tbEliminados + " Tipos de bebida eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	/* ****************************************************************
	 * 			Demos de Cliente
	 *****************************************************************/
    /**
     * Demostraci�n de creaci�n, consulta y borrado de Bebidas.
     * Incluye tambi�n los tipos de bebida pues el tipo de bebida es llave for�nea en las bebidas
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    @SuppressWarnings ("unused")
	public void demoCliente( )
    {
    	try 
    	{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
    		long id = 76798;
    		int documento= 870;
    		int nit = 98;
    		String nombre = "Pepito";
    		String correo = "pepito@gmail.com";
    		String direccion = "carrera 17 # 34-65";
    		String tipo = "empresa";
			boolean errorCliente = false;
			VOCliente tipoBebida = superandes.registrarCliente(documento, nit, nombre, direccion, correo, tipo);
			if (tipoBebida == null)
			{
				tipoBebida = superandes.darClientePorId(id);
				errorCliente = true;
			}
			
			List <VOCliente> listaCliente = superandes.darVOCliente();
			long tbEliminados = superandes.eliminarCliente(id);
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Bebidas\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n\n";
			if (errorCliente)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado += "Adicionado el tipo de bebida con nombre: " + id + "\n";
			resultado += "Adicionada la bebida con nombre: " + documento + "\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += tbEliminados + " Tipos de bebida eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
    	catch (Exception e) 
    	{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }
    
	/* ****************************************************************
	 * 			Demos de Estante
	 *****************************************************************/
    /**
     * Demostraci�n de creaci�n, consulta y borrado de Bares
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoEstante ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			VOEstante estante =superandes.registrarEstante(12, 3454, 23, "Congelados", "esto");
			
			List <VOEstante> lista =superandes.darVOEstante();
			
			long baresEliminados = superandes.eliminarEstante(12);
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Bares\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "Adicionado el bar: " + estante + "\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarEstante(lista);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += baresEliminados + " Bares eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }



	/* ****************************************************************
	 * 			Demos de Pedido 
	 *****************************************************************/
    /**
     * Demostraci�n de creaci�n, consulta y borrado de Bebedores
     * Incluye la consulta por nombre y por id
     * Incluye el borrado por nombre
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    @SuppressWarnings ("unused")
	public void demoPedido ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			VOPedido bdor1 = superandes.registrarPedido(new Date(30/10/2018), new Date (31/10/2018), "esto", 709, "proceso");
			VOPedido bdor2 = superandes.registrarPedido(new Date(01/11/2018), new Date (03/11/2018), "esto", 543, "proceso");
			VOPedido bdor3 = superandes.registrarPedido(new Date(25/10/2018), new Date(27/10/2018), "para", 423, "entregado");
			
			List <VOPedido> pedidos =superandes.darVOPedido();

			long bdor1Eliminados = superandes.eliminarPedido(bdor1.getIdPedido());
			long bdor2Eliminados = superandes.eliminarPedido(bdor2.getIdPedido());
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Bebedores\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "\n" + listarPedido (pedidos);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\nBuscando el bebedor con id " + bdor1 + ":\n";
			resultado += bdor1 != null ? "El bebedor es: " + bdor1 + "\n" : "Ese bebedor no existe\n";
			resultado += "\nBuscando el bebedor con id " + 0 + ":\n";
			resultado += bdor2 != null ? "El bebedor es: " + bdor2 + "\n" : "Ese bebedor no existe\n";
			
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += bdor1Eliminados + " Pepitos eliminados\n";
			resultado += bdor2Eliminados + " Pedritos eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }


 
	/* ****************************************************************
	 * 			Demos de Producto
	 *****************************************************************/
    /**
     * Demostraci�n de creaci�n, consulta y borrado de la relaci�n Gustan
     * Incluye el manejo de Bebedores
     * Incluye el manejo de Bebidas
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoProducto ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			boolean errorTipoBebida = false;
			VOProducto tipoBebida = superandes.adicionarProducto(6587, "Coca cola", "Coca Cola", 3.500, 2.000 , "ml", 350, 23, "7459948", new Date(02/12/2018), 32, 3.500, 8405);
			if (tipoBebida == null)
			{
				tipoBebida = superandes.darProductoPorId(6587);
				errorTipoBebida = true;
			}

			List <VOProducto> listaGustan = superandes.darVOProducto();
			long sEliminados = superandes.eliminarProducto(6587);
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Gustan\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarProducto (listaGustan);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += sEliminados + " Bebedores eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	/* ****************************************************************
	 * 			Demos de Promocion
	 *****************************************************************/
    /**
     * Demostraci�n de creaci�n, consulta y borrado de la relaci�n Sirven
     * Incluye el manejo de Bares
     * Incluye el manejo de tipos de bebida
     * Incluye el manejo de Bebidas
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoPromocion ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			boolean errorTipoBebida = false;
			VOPromocion tipoBebida = superandes.adicionarPromocion(9874, "2x1", new Date(31/10/2018), new Date(10/11/2018));
			if (tipoBebida == null)
			{
				tipoBebida = superandes.darPromocionPorId(9874);
				errorTipoBebida = true;
			}

			List <VOPromocion> listaSirven = superandes.darVOPromocion();

			long promosEliminados = superandes.eliminarPromocion(9874);
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Sirven\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}

			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarPromocion (listaSirven);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += promosEliminados + " Bares eliminados\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
    }

	/* ****************************************************************
	 * 			Demos de Proveedor
	 *****************************************************************/
    /**
     * Demostraci�n de creaci�n, consulta y borrado de la relaci�n Visitan
     * Incluye el manejo de Bebedores
     * Incluye el manejo de Bares
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoProveedor ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			VOProveedor bar1 = superandes.adicionarProveedor(9803, "jojo", 4.3);

			List <VOProveedor> listaVisitan = superandes.darVOProveedor();
			long proveedoresEliminadas = superandes.eliminarProveedor(9803);
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Visitan\n\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarProveedor (listaVisitan);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += proveedoresEliminadas + " Bebedores eliminadas\n";
			resultado += "\n Demo terminada";
   
			panelDatos.actualizarInterfaz(resultado);
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
		mostrarArchivo ("superandes.log");
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
	public void limpiarLogSuperandes ()
	{
		// Ejecuci�n de la operaci�n y recolecci�n de los resultados
		boolean resp = limpiarArchivo ("superandes.log");

		// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
		String resultado = "\n\n************ Limpiando el log de superandes ************ \n";
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
		mostrarArchivo ("data/EsquemaParranderos.txt");
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
     * Genera una cadena de caracteres con la lista de bebidas recibida: una l�nea por cada bebida
     * @param lista - La lista con las bebidas
     * @return La cadena con una l�ea para cada bebida recibida
     */
    private String listarCliente (List<VOCliente> lista) 
    {
    	String resp = "Las bebidas existentes son:\n";
    	int i = 1;
        for (VOCliente beb : lista)
        {
        	resp += i++ + ". " + beb.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de bebedores recibida: una l�nea por cada bebedor
     * @param lista - La lista con los bebedores
     * @return La cadena con una l�ea para cada bebedor recibido
     */
    private String listarEstante (List<VOEstante> lista) 
    {
    	String resp = "Los bebedores existentes son:\n";
    	int i = 1;
        for (VOEstante bdor : lista)
        {
        	resp += i++ + ". " + bdor.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de bares recibida: una l�nea por cada bar
     * @param lista - La lista con los bares
     * @return La cadena con una l�ea para cada bar recibido
     */
    private String listarPedido (List<VOPedido> lista) 
    {
    	String resp = "Los bares existentes son:\n";
    	int i = 1;
        for (VOPedido bar : lista)
        {
        	resp += i++ + ". " + bar.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de gustan recibida: una l�nea por cada gusta
     * @param lista - La lista con los gustan
     * @return La cadena con una l�ea para cada gustan recibido
     */
    private String listarProducto (List<VOProducto> lista) 
    {
    	String resp = "Los gustan existentes son:\n";
    	int i = 1;
        for (VOProducto serv : lista)
        {
        	resp += i++ + ". " + serv.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de sirven recibida: una l�nea por cada sirven
     * @param lista - La lista con los sirven
     * @return La cadena con una l�ea para cada sirven recibido
     */
    private String listarPromocion (List<VOPromocion> lista) 
    {
    	String resp = "Los sirven existentes son:\n";
    	int i = 1;
        for (VOPromocion serv : lista)
        {
        	resp += i++ + ". " + serv.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de visitan recibida: una l�nea por cada visitan
     * @param lista - La lista con los visitan
     * @return La cadena con una l�ea para cada visitan recibido
     */
    private String listarProveedor (List<VOProveedor> lista) 
    {
    	String resp = "Los visitan existentes son:\n";
    	int i = 1;
        for (VOProveedor vis : lista)
        {
        	resp += i++ + ". " + vis.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de visitan recibida: una l�nea por cada visitan
     * @param lista - La lista con los visitan
     * @return La cadena con una l�ea para cada visitan recibido
     */
    private String listarSucursal (List<VOSucursal> lista) 
    {
    	String resp = "Los visitan existentes son:\n";
    	int i = 1;
        for (VOSucursal vis : lista)
        {
        	resp += i++ + ". " + vis.toString() + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de visitan recibida: una l�nea por cada visitan
     * @param lista - La lista con los visitan
     * @return La cadena con una l�ea para cada visitan recibido
     */
    private String listarVenta (List<VOVenta> lista) 
    {
    	String resp = "Los visitan existentes son:\n";
    	int i = 1;
        for (VOVenta vis : lista)
        {
        	resp += i++ + ". " + vis.toString() + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de parejas de n�meros recibida: una l�nea por cada pareja
     * @param lista - La lista con las pareja
     * @return La cadena con una l�ea para cada pareja recibido
     */
    private String listarSucursalesyNumeroBodegas (List<long[]> lista) 
    {
    	String resp = "Las sucursales y el n�mero de bodegas que tiene son:\n";
    	int i = 1;
        for ( long [] tupla : lista)
        {
			long [] datos = tupla;
	        String resp1 = i++ + ". " + "[";
			resp1 += "idBar: " + datos [0] + ", ";
			resp1 += "numBebidas: " + datos [1];
	        resp1 += "]";
	        resp += resp1 + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de parejas de n�meros recibida: una l�nea por cada pareja
     * @param lista - La lista con las pareja
     * @return La cadena con una l�ea para cada pareja recibido
     */
    private String listarSucursalesyNumeroEstantes (List<long[]> lista) 
    {
    	String resp = "Las sucursales y el n�mero de bodegas que tiene son:\n";
    	int i = 1;
        for ( long [] tupla : lista)
        {
			long [] datos = tupla;
	        String resp1 = i++ + ". " + "[";
			resp1 += "idBar: " + datos [0] + ", ";
			resp1 += "numBebidas: " + datos [1];
	        resp1 += "]";
	        resp += resp1 + "\n";
        }
        return resp;
	}
    
    /**
     * Genera una cadena de caracteres con la lista de parejas de n�meros recibida: una l�nea por cada pareja
     * @param lista - La lista con las pareja
     * @return La cadena con una l�ea para cada pareja recibido
     */
    private String listarBodegayNumeroProductos (List<long[]> lista) 
    {
    	String resp = "Las sucursales y el n�mero de bodegas que tiene son:\n";
    	int i = 1;
        for ( long [] tupla : lista)
        {
			long [] datos = tupla;
	        String resp1 = i++ + ". " + "[";
			resp1 += "idBar: " + datos [0] + ", ";
			resp1 += "numBebidas: " + datos [1];
	        resp1 += "]";
	        resp += resp1 + "\n";
        }
        return resp;
	}

    /**
     * Genera una cadena de caracteres con la lista de parejas de n�meros recibida: una l�nea por cada pareja
     * @param lista - La lista con las pareja
     * @return La cadena con una l�ea para cada pareja recibido
     */
    private String listarEstanteyNumeroProductos (List<long[]> lista) 
    {
    	String resp = "Las sucursales y el n�mero de bodegas que tiene son:\n";
    	int i = 1;
        for ( long [] tupla : lista)
        {
			long [] datos = tupla;
	        String resp1 = i++ + ". " + "[";
			resp1 += "idBar: " + datos [0] + ", ";
			resp1 += "numBebidas: " + datos [1];
	        resp1 += "]";
	        resp += resp1 + "\n";
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
			Method req = InterfazDemo.class.getMethod ( evento );			
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
            InterfazDemo interfaz = new InterfazDemo( );
            interfaz.setVisible( true );
        }
        catch( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
