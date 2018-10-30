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
			long tbEliminados = parranderos.eliminarTipoBebidaPorId (bodega.getId ());
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de TipoBebida\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado += "Adicionado el tipo de bebida con nombre: " + nombreTipoBebida + "\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado +=  "\n" + listarTiposBebida (lista);
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
	 * 			Demos de Bebida
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
	public void demoBebida( )
    {
    	try 
    	{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			String nombreTipoBebida = "Vino tinto";
			String nombreBebida = "120";
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida (nombreTipoBebida);
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre (nombreTipoBebida);
				errorTipoBebida = true;
			}
			VOBebida bebida = parranderos.adicionarBebida(nombreBebida, tipoBebida.getId (), 10);
			
			List <VOTipoBebida> listaTiposBebida = parranderos.darVOTiposBebida();
			List <VOBebida> listaBebidas = parranderos.darVOBebidas();
			long bebEliminadas = parranderos.eliminarBebidaPorNombre(nombreBebida);
			long tbEliminados = parranderos.eliminarTipoBebidaPorId (tipoBebida.getId ());
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Bebidas\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n\n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado += "Adicionado el tipo de bebida con nombre: " + nombreTipoBebida + "\n";
			resultado += "Adicionada la bebida con nombre: " + nombreBebida + "\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado +=  "\n" + listarTiposBebida (listaTiposBebida);
			resultado += "\n" + listarBebidas (listaBebidas);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += bebEliminadas + " Bebidas eliminadas\n";
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

    /**
     * Demostraci�n de creaci�n y borrado de bebidas no servidas.
     * Incluye tambi�n los tipos de bebida pues el tipo de bebida es llave for�nea en las bebidas
     * Caso 1: Ninguna bebida es servida en ning�n bar: La tabla de bebidas queda vac�a antes de la fase de limpieza
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoElimNoServidas1 ( )
    {
    	try 
    	{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			String nombreTipoBebida = "Vino tinto";
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida (nombreTipoBebida);
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre (nombreTipoBebida);
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida("120", tipoBebida.getId (), 10);
			VOBebida bebida2 = parranderos.adicionarBebida("Gato Negro", tipoBebida.getId (), 11);
			VOBebida bebida3 = parranderos.adicionarBebida("Don Pedro", tipoBebida.getId (), 12);
			
			List <VOTipoBebida> listaTiposBebida = parranderos.darVOTiposBebida();
			List <VOBebida> listaBebidas1 = parranderos.darVOBebidas();
			List <VOSirven> listaSirven = parranderos.darVOSirven ();
			long noServidasEliminadas = parranderos.eliminarBebidasNoServidas();
			List <VOBebida> listaBebidas2 = parranderos.darVOBebidas();
			
			long bebEliminadas1 = parranderos.eliminarBebidaPorId(bebida1.getId ());
			long bebEliminadas2 = parranderos.eliminarBebidaPorId(bebida2.getId ());
			long bebEliminadas3 = parranderos.eliminarBebidaPorId(bebida3.getId ());
			long tbEliminados = parranderos.eliminarTipoBebidaPorNombre (nombreTipoBebida);
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de borrado de las bebidas no servidas 1\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado +=  "\n" + listarTiposBebida (listaTiposBebida);
			resultado += "\n" + listarBebidas (listaBebidas1);
			resultado += "\n" + listarSirven (listaSirven);
			resultado += "\n\n************ Ejecutando la demo: Borrando bebidas no servidas ************ \n";
			resultado += noServidasEliminadas + " Bebidas eliminadas\n";
			resultado += "\n" + listarBebidas (listaBebidas2);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += (bebEliminadas1 + bebEliminadas2 + bebEliminadas3) + " Bebidas eliminadas\n";
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

    /**
     * Demostraci�n de creaci�n y borrado de bebidas no servidas.
     * Incluye tambi�n los tipos de bebida pues el tipo de bebida es llave for�nea en las bebidas
     * Incluye tambi�n los bares pues son llave for�nea en la relaci�n Sirven
     * Caso 2: Hay bebidas que son servidas y estas quedan en la tabla de bebidas
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoElimNoServidas2 ( )
    {
    	try 
    	{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			String nombreTipoBebida = "Vino tinto";
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida (nombreTipoBebida);
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre (nombreTipoBebida);
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida("120", tipoBebida.getId (), 10);
			VOBebida bebida2 = parranderos.adicionarBebida("Gato Negro", tipoBebida.getId (), 11);
			VOBebida bebida3 = parranderos.adicionarBebida("Don Pedro", tipoBebida.getId (), 12);
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos", "Bogot�", "Bajo", 2);
			parranderos.adicionarSirven (bar1.getId (), bebida1.getId (), "diurno");
			
			List <VOTipoBebida> listaTiposBebida = parranderos.darVOTiposBebida();
			List <VOBebida> listaBebidas1 = parranderos.darVOBebidas();
			List <VOBar> bares = parranderos.darVOBares ();
			List <VOSirven> sirven = parranderos.darVOSirven ();
			long noServidasEliminadas = parranderos.eliminarBebidasNoServidas();
			List <VOBebida> listaBebidas2 = parranderos.darVOBebidas();
			
			long sirvenEliminados = parranderos.eliminarSirven(bar1.getId (), bebida1.getId ());
			long bebEliminadas1 = parranderos.eliminarBebidaPorId(bebida1.getId ());
			long bebEliminadas2 = parranderos.eliminarBebidaPorId(bebida2.getId ());
			long bebEliminadas3 = parranderos.eliminarBebidaPorId(bebida3.getId ());
			long tbEliminados = parranderos.eliminarTipoBebidaPorNombre (nombreTipoBebida);
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos");
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de borrado de las bebidas no servidas 2\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado += "\n" + listarTiposBebida (listaTiposBebida);
			resultado += "\n" + listarBebidas (listaBebidas1);
			resultado += "\n" + listarBares (bares);
			resultado += "\n" + listarSirven (sirven);
			resultado += "\n\n************ Ejecutando la demo: Borrando bebidas no servidas ************ \n";
			resultado += noServidasEliminadas + " Bebidas eliminadas\n";
			resultado += "\n" + listarBebidas (listaBebidas2);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += sirvenEliminados + " Sirven eliminados\n";
			resultado += baresEliminados + " Bares eliminados\n";
			resultado += (bebEliminadas1 + bebEliminadas2 + bebEliminadas3) + " Bebidas eliminadas\n";
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
	 * 			Demos de Bar
	 *****************************************************************/
    /**
     * Demostraci�n de creaci�n, consulta y borrado de Bares
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoBar ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos", "Bogot�", "Bajo", 2);
			
			List <VOBar> lista = parranderos.darVOBares ();
			
			long baresEliminados = parranderos.eliminarBarPorNombre("Los Amigos");
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Bares\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "Adicionado el bar: " + bar1 + "\n";
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarBares (lista);
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

    /**
     * Demostraci�n de la consulta: Dar el id y el n�mero de bebidas que sirve cada bar, siempre y cuando el bar sirva por los menos una bebida
     * Incluye el manejo de los tipos de bebida pues el tipo de bebida es llave for�nea en las bebidas
     * Incluye el manajo de las bebidas
     * Incluye el manejo de los bares
     * Incluye el manejo de la relaci�n sirven
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoBaresBebidas ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida ("Vino tinto");
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre ("Vino tinto");
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida ("120", tipoBebida.getId (), 10);
			VOBebida bebida2 = parranderos.adicionarBebida ("121", tipoBebida.getId (), 10);
			VOBebida bebida3 = parranderos.adicionarBebida ("122", tipoBebida.getId (), 10);
			VOBebida bebida4 = parranderos.adicionarBebida ("123", tipoBebida.getId (), 10);
			VOBebida bebida5 = parranderos.adicionarBebida ("124", tipoBebida.getId (), 10);
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos1", "Bogot�", "Bajo", 2);
			VOBar bar2 = parranderos.adicionarBar ("Los Amigos2", "Bogot�", "Bajo", 3);
			VOBar bar3 = parranderos.adicionarBar ("Los Amigos3", "Bogot�", "Bajo", 4);
			VOBar bar4 = parranderos.adicionarBar ("Los Amigos4", "Medell�n", "Bajo", 5);
			parranderos.adicionarSirven (bar1.getId (), bebida1.getId (), "diurno");
			parranderos.adicionarSirven (bar1.getId (), bebida2.getId (), "diurno");
			parranderos.adicionarSirven (bar2.getId (), bebida1.getId (), "diurno");
			parranderos.adicionarSirven (bar2.getId (), bebida2.getId (), "diurno");
			parranderos.adicionarSirven (bar2.getId (), bebida3.getId (), "diurno");
			parranderos.adicionarSirven (bar3.getId (), bebida1.getId (), "diurno");
			parranderos.adicionarSirven (bar3.getId (), bebida2.getId (), "diurno");
			parranderos.adicionarSirven (bar3.getId (), bebida3.getId (), "diurno");
			parranderos.adicionarSirven (bar3.getId (), bebida4.getId (), "diurno");
			parranderos.adicionarSirven (bar3.getId (), bebida5.getId (), "diurno");
			
			List <VOTipoBebida> listaTiposBebida = parranderos.darVOTiposBebida ();
			List <VOBebida> listaBebidas = parranderos.darVOBebidas ();
			List <VOBar> listaBares = parranderos.darVOBares ();
			List <VOSirven> listaSirven = parranderos.darVOSirven ();

			List <long []> listaByB = parranderos.darBaresYCantidadBebidasSirven();

			long sirvenEliminados = parranderos.eliminarSirven (bar1.getId (), bebida1.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar1.getId (), bebida2.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar2.getId (), bebida1.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar2.getId (), bebida2.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar2.getId (), bebida3.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar3.getId (), bebida1.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar3.getId (), bebida2.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar3.getId (), bebida3.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar3.getId (), bebida4.getId ());
			sirvenEliminados += parranderos.eliminarSirven (bar3.getId (), bebida5.getId ());
			long bebidasEliminadas = parranderos.eliminarBebidaPorNombre ("120");
			bebidasEliminadas += parranderos.eliminarBebidaPorNombre ("121");
			bebidasEliminadas += parranderos.eliminarBebidaPorNombre ("122");
			bebidasEliminadas += parranderos.eliminarBebidaPorNombre ("123");
			bebidasEliminadas += parranderos.eliminarBebidaPorNombre ("124");
			long tbEliminados = parranderos.eliminarTipoBebidaPorNombre ("Vino tinto");
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos1");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos2");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos3");
			baresEliminados += parranderos.eliminarBarPorId (bar4.getId ());
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Bares y cantidad de visitas que reciben\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado += "\n" + listarTiposBebida (listaTiposBebida);
			resultado += "\n" + listarBebidas (listaBebidas);
			resultado += "\n" + listarBares (listaBares);
			resultado += "\n" + listarSirven (listaSirven);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarBaresYBebidas (listaByB);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += sirvenEliminados + " Sirven eliminados\n";
			resultado += bebidasEliminadas + " Bebidas eliminados\n";
			resultado += tbEliminados + " Tipos de Bebida eliminados\n";
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

    /**
     * Demostraci�n de la modificaci�n: Aumentar en uno el n�mero de sedes de los bares de una ciudad
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoAumentarSedesBaresEnCiudad ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos1", "Bogot�", "Bajo", 2);
			VOBar bar2 = parranderos.adicionarBar ("Los Amigos2", "Bogot�", "Bajo", 3);
			VOBar bar3 = parranderos.adicionarBar ("Los Amigos3", "Bogot�", "Bajo", 4);
			VOBar bar4 = parranderos.adicionarBar ("Los Amigos4", "Medell�n", "Bajo", 5);
			List <VOBar> listaBares = parranderos.darVOBares ();
			
			long baresModificados = parranderos.aumentarSedesBaresCiudad("Bogot�");
			List <VOBar> listaBares2 = parranderos.darVOBares ();

			long baresEliminados = parranderos.eliminarBarPorId (bar1.getId ());
			baresEliminados += parranderos.eliminarBarPorId (bar2.getId ());
			baresEliminados += parranderos.eliminarBarPorId (bar3.getId ());
			baresEliminados += parranderos.eliminarBarPorId (bar4.getId ());
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo

			String resultado = "Demo de modificaci�n n�mero de sedes de los bares de una ciudad\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "\n" + listarBares (listaBares);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += baresModificados + " Bares modificados\n";
			resultado += "\n" + listarBares (listaBares2);
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
	 * 			Demos de Bebedor
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
	public void demoBebedor ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogot�", "Alto");
			VOBebedor bdor2 = parranderos.adicionarBebedor ("Pepito", "Medell�n", "Alto");
			VOBebedor bdor3 = parranderos.adicionarBebedor ("Pedrito", "Cali", "Alto");
			
			List <VOBebedor> bebedores = parranderos.darVOBebedores();
			VOBebedor bdor5 = parranderos.darBebedorPorId(bdor1.getId ());
			VOBebedor bdor6 = parranderos.darBebedorPorId(0);
			List <VOBebedor> pepitos = parranderos.darVOBebedoresPorNombre("Pepito");
			List <VOBebedor> pedritos = parranderos.darVOBebedoresPorNombre("Pedrito");
			
			long pepitosEliminados = parranderos.eliminarBebedorPorNombre ("Pepito");
			long pedritosEliminados = parranderos.eliminarBebedorPorNombre ("Pedrito");

			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Bebedores\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "\n" + listarBebedores (bebedores);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\nBuscando el bebedor con id " + bdor1 + ":\n";
			resultado += bdor5 != null ? "El bebedor es: " + bdor5 + "\n" : "Ese bebedor no existe\n";
			resultado += "\nBuscando el bebedor con id " + 0 + ":\n";
			resultado += bdor6 != null ? "El bebedor es: " + bdor6 + "\n" : "Ese bebedor no existe\n";
			resultado += "\nBuscando los bebedores con nombre Pepito:";
			resultado += "\n" + listarBebedores (pepitos);
			resultado += "\nBuscando los bebedores con nombre Pedrito:";
			resultado += "\n" + listarBebedores (pedritos);
			
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += pepitosEliminados + " Pepitos eliminados\n";
			resultado += pedritosEliminados + " Pedritos eliminados\n";
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

    /**
     * Demostraci�n de creaci�n, consulta de TODA LA INFORMACI�N de un bebedor y borrado de un bebedor y sus visitas
     * Incluye el manejo de tipos de bebida
     * Incluye el manejo de bebidas
     * Incluye el manejo de bares
     * Incluye el manejo de la relaci�n sirven
     * Incluye el manejo de la relaci�n gustan
     * Incluye el borrado de un bebedor y todas sus visitas
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoDarBebedorCompleto ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados.
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida ("Vino tinto");
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre ("Vino tinto");
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida ("120", tipoBebida.getId (), 10);
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos", "Bogot�", "Bajo", 2);
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogot�", "Alto");
			
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "nocturno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "todos");
			parranderos.adicionarGustan (bdor1.getId (), bebida1.getId ());

			List <VOTipoBebida> listaTipos = parranderos.darVOTiposBebida();
			List <VOBebida> listaBebidas = parranderos.darVOBebidas();
			List <VOBar> listaBares = parranderos.darVOBares ();
			List <VOBebedor> bebedores = parranderos.darVOBebedores();
			List <VOGustan> listaGustan = parranderos.darVOGustan();
			List <VOVisitan> listaVisitan = parranderos.darVOVisitan();

			VOBebedor bdor2 = parranderos.darBebedorCompleto(bdor1.getId ());

			long gustanEliminados = parranderos.eliminarGustan (bdor1.getId (), bebida1.getId ());
			long bebidasEliminadas = parranderos.eliminarBebidaPorNombre ("120");
			long tiposEliminados = parranderos.eliminarTipoBebidaPorNombre ("Vino tinto");
			long [] bebedorVisitasEliminados = parranderos.eliminarBebedorYVisitas_v1 (bdor1.getId ());
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos");

			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de toda la informaci�n de un bebedor\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado += "\n" + listarTiposBebida (listaTipos);
			resultado += "\n" + listarBebidas (listaBebidas);
			resultado += "\n" + listarBares (listaBares);
			resultado += "\n" + listarBebedores (bebedores);
			resultado += "\n" + listarGustan (listaGustan);
			resultado += "\n" + listarVisitan (listaVisitan);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\nBuscando toda la informaci�n del bebedor con id " + bdor1 + ":\n";
			resultado += bdor2 != null ? "El bebedor es: " + bdor2.toStringCompleto() + "\n" : "Ese bebedor no existe\n";	
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += gustanEliminados + " Gustan eliminados\n";
			resultado += bebidasEliminadas + " Bebidas eliminadas\n";
			resultado += tiposEliminados + " Tipos de Bebida eliminados\n";
			resultado += bebedorVisitasEliminados [0] + " Bebedores eliminados y " + bebedorVisitasEliminados [1] +" Visitas eliminadas\n";
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

    /**
     * Demostraci�n de creaci�n, consulta de TODA LA INFORMACI�N de un bebedor y borrado de un bebedor y sus visitas.
     * Si hay posibilidades de alguna incoherencia con esta operaci�n NO SE BORRA NI EL BEBEDOR NI SUS VISITAS
     * Incluye el manejo de tipos de bebida
     * Incluye el manejo de bebidas
     * Incluye el manejo de bares
     * Incluye el manejo de la relaci�n sirven
     * Incluye el manejo de la relaci�n gustan
     * Incluye el borrado de un bebedor y todas sus visitas v1
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos queda con las tuplas que no se pudieron borrar: ES COHERENTE DE TODAS MANERAS
     */
    public void demoEliminarBebedorYVisitas_v1 ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados.
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogot�", "Alto");
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos", "Bogot�", "Bajo", 2);
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida ("Vino tinto");
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre ("Vino tinto");
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida ("120", tipoBebida.getId (), 10);
			
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "nocturno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "todos");
			parranderos.adicionarGustan (bdor1.getId (), bebida1.getId ());

			List <VOTipoBebida> listaTipos = parranderos.darVOTiposBebida();
			List <VOBebida> listaBebidas = parranderos.darVOBebidas();
			List <VOBar> listaBares = parranderos.darVOBares ();
			List <VOBebedor> bebedores = parranderos.darVOBebedores();
			List <VOGustan> listaGustan = parranderos.darVOGustan();
			List <VOVisitan> listaVisitan = parranderos.darVOVisitan();

			VOBebedor bdor2 = parranderos.darBebedorCompleto(bdor1.getId ());

			// No se elimina la tupla de GUSTAN para estudiar la coherencia de las operaciones en la base de daatos
			long gustanEliminados = 0;
			long bebidasEliminadas = parranderos.eliminarBebidaPorNombre ("120");
			long tiposEliminados = parranderos.eliminarTipoBebidaPorNombre ("Vino tinto");
			long [] bebedorVisitasEliminados = parranderos.eliminarBebedorYVisitas_v1 (bdor1.getId ());
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos");

			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de toda la informaci�n de un bebedor\n";
			resultado += "Y DE BORRADO DE BEBEDOR Y VISITAS cuando el bebedor a�n est� referenciado cuando se quiere borrar\n";
			resultado += "v1: No se borra NI el bebedor NI sus visitas";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado += "\n" + listarTiposBebida (listaTipos);
			resultado += "\n" + listarBebidas (listaBebidas);
			resultado += "\n" + listarBares (listaBares);
			resultado += "\n" + listarBebedores (bebedores);
			resultado += "\n" + listarGustan (listaGustan);
			resultado += "\n" + listarVisitan (listaVisitan);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\nBuscando toda la informaci�n del bebedor con id " + bdor1 + ":\n";
			resultado += bdor2 != null ? "El bebedor es: " + bdor2.toStringCompleto() + "\n" : "Ese bebedor no existe\n";	
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += gustanEliminados + " Gustan eliminados\n";
			resultado += bebidasEliminadas + " Bebidas eliminadas\n";
			resultado += tiposEliminados + " Tipos de Bebida eliminados\n";
			resultado += bebedorVisitasEliminados [0] + " Bebedores eliminados y " + bebedorVisitasEliminados [1] +" Visitas eliminadas\n";
			resultado += baresEliminados + " Bares eliminados\n";
			resultado += "\n\n************ ATENCI�N - ATENCI�N - ATENCI�N - ATENCI�N ************ \n";
			resultado += "\nRecuerde que -1 registros borrados significa que hubo un problema !! \n";
			resultado += "\nREVISE EL LOG DE PARRANDEROS Y EL DE DATANUCLEUS \n";
			resultado += "\nNO OLVIDE LIMPIAR LA BASE DE DATOS \n";
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

    /**
     * Demostraci�n de creaci�n, consulta de TODA LA INFORMACI�N de un bebedor y borrado de un bebedor y sus visitas
     * Si hay posibilidades de alguna incoherencia con esta operaci�n SE BORRA LO AQUELLO QUE SEA POSIBLE, 
     * PERO CONSERVANDO LA COHERENCIA DE LA BASE DE DATOS
     * Incluye el manejo de tipos de bebida
     * Incluye el manejo de bebidas
     * Incluye el manejo de bares
     * Incluye el manejo de la relaci�n sirven
     * Incluye el manejo de la relaci�n gustan
     * Incluye el borrado de un bebedor y todas sus visitas v2
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos queda con las tuplas que no se pudieron borrar
     */
    public void demoEliminarBebedorYVisitas_v2 ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados.
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogot�", "Alto");
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos", "Bogot�", "Bajo", 2);
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida ("Vino tinto");
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre ("Vino tinto");
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida ("120", tipoBebida.getId (), 10);
			
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "nocturno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "todos");
			parranderos.adicionarGustan (bdor1.getId (), bebida1.getId ());

			List <VOTipoBebida> listaTipos = parranderos.darVOTiposBebida();
			List <VOBebida> listaBebidas = parranderos.darVOBebidas();
			List <VOBar> listaBares = parranderos.darVOBares ();
			List <VOBebedor> bebedores = parranderos.darVOBebedores();
			List <VOGustan> listaGustan = parranderos.darVOGustan();
			List <VOVisitan> listaVisitan = parranderos.darVOVisitan();

			VOBebedor bdor2 = parranderos.darBebedorCompleto(bdor1.getId ());

			// No se elimina la tupla de GUSTAN para estudiar la coherencia de las operaciones en la base de daatos
			long gustanEliminados = 0;
			long bebidasEliminadas = parranderos.eliminarBebidaPorNombre ("120");
			long tiposEliminados = parranderos.eliminarTipoBebidaPorNombre ("Vino tinto");
			long [] bebedorVisitasEliminados = parranderos.eliminarBebedorYVisitas_v2 (bdor1.getId ());
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos");

			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de toda la informaci�n de un bebedor\n";
			resultado += "Y DE BORRADO DE BEBEDOR Y VISITA,S cuando el bebedor a�n est� referenciado cuando se quiere borrar\n";
			resultado += "v2: El bebedor no se puede borrar, pero sus visitas S�";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado += "\n" + listarTiposBebida (listaTipos);
			resultado += "\n" + listarBebidas (listaBebidas);
			resultado += "\n" + listarBares (listaBares);
			resultado += "\n" + listarBebedores (bebedores);
			resultado += "\n" + listarGustan (listaGustan);
			resultado += "\n" + listarVisitan (listaVisitan);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\nBuscando toda la informaci�n del bebedor con id " + bdor1 + ":\n";
			resultado += bdor2 != null ? "El bebedor es: " + bdor2.toStringCompleto() + "\n" : "Ese bebedor no existe\n";	
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += gustanEliminados + " Gustan eliminados\n";
			resultado += bebidasEliminadas + " Bebidas eliminadas\n";
			resultado += tiposEliminados + " Tipos de Bebida eliminados\n";
			resultado += bebedorVisitasEliminados [0] + " Bebedores eliminados y " + bebedorVisitasEliminados [1] +" Visitas eliminadas\n";
			resultado += baresEliminados + " Bares eliminados\n";
			resultado += "\n\n************ ATENCI�N - ATENCI�N - ATENCI�N - ATENCI�N ************ \n";
			resultado += "\nRecuerde que -1 registros borrados significa que hubo un problema !! \n";
			resultado += "\nREVISE EL LOG DE PARRANDEROS Y EL DE DATANUCLEUS \n";
			resultado += "\nNO OLVIDE LIMPIAR LA BASE DE DATOS \n";
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

    /**
     * Demostraci�n de la modificaci�n: Cambiar la ciudad de un bebedor
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
    */
    public void demoCambiarCiudadBebedor ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogot�", "Alto");
			
			List<VOBebedor> bebedores1 = parranderos.darVOBebedores ();
			long bebedoresActualizados = parranderos.cambiarCiudadBebedor (bdor1.getId (), "Medell�n");
			List<VOBebedor> bebedores2 = parranderos.darVOBebedores ();
			
			long bebedoresEliminados = parranderos.eliminarBebedorPorNombre ("Pepito");

			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de modificaci�n de la ciudad de un bebedor\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "\n" + listarBebedores (bebedores1);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += bebedoresActualizados + " Bebedores modificados\n";
			resultado += "\n" + listarBebedores (bebedores2);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += bebedoresEliminados + " Bebedores eliminados\n";
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

    /**
     * Demostraci�n de la consulta: Dar la informaci�n de los bebedores y del n�mero de bares que visita cada uno
     * Incluye el manejo de los bares
     * Incuye el manejo de la relaci�n visitan
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoBebedoresYNumVisitasRealizadas ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos1", "Bogot�", "Bajo", 2);
			VOBar bar2 = parranderos.adicionarBar ("Los Amigos2", "Bogot�", "Bajo", 3);
			VOBar bar3 = parranderos.adicionarBar ("Los Amigos3", "Bogot�", "Bajo", 4);
			VOBar bar4 = parranderos.adicionarBar ("Los Amigos4", "Medell�n", "Bajo", 5);
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogot�", "Alto");
			VOBebedor bdor2 = parranderos.adicionarBebedor ("Juanito", "Bogot�", "Alto");
			VOBebedor bdor3 = parranderos.adicionarBebedor ("Carlitos", "Medell�n", "Alto");
			VOBebedor bdor4 = parranderos.adicionarBebedor ("Luis", "Cartagena", "Medio");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "nocturno");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "todos");
			parranderos.adicionarVisitan (bdor1.getId (), bar2.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar3.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor2.getId (), bar3.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor2.getId (), bar4.getId (), new Timestamp (System.currentTimeMillis()), "diurno");

			List<VOBar> bares = parranderos.darVOBares();
			List<VOBebedor> bebedores = parranderos.darVOBebedores();
			List<VOVisitan> visitan = parranderos.darVOVisitan ();
			List<Object []> bebedoresYNumVisitas = parranderos.darBebedoresYNumVisitasRealizadas ();

			long [] elimBdor1 = parranderos.eliminarBebedorYVisitas_v1 (bdor1.getId ());
			long [] elimBdor2 = parranderos.eliminarBebedorYVisitas_v1 (bdor2.getId ());
			long [] elimBdor3 = parranderos.eliminarBebedorYVisitas_v1 (bdor3.getId ());
			long [] elimBdor4 = parranderos.eliminarBebedorYVisitas_v1 (bdor4.getId ());
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos1");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos2");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos3");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos4");

			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de dar bebedores y cu�ntas visitan han realizado\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "\n" + listarBares (bares);
			resultado += "\n" + listarBebedores (bebedores);
			resultado += "\n" + listarVisitan (visitan);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarBebedorYNumVisitas (bebedoresYNumVisitas);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += elimBdor1 [0] + " Bebedores eliminados y " + elimBdor1 [1] +" Visitas eliminadas\n";
			resultado += elimBdor2 [0] + " Bebedores eliminados y " + elimBdor2 [1] +" Visitas eliminadas\n";
			resultado += elimBdor3 [0] + " Bebedores eliminados y " + elimBdor3 [1] +" Visitas eliminadas\n";
			resultado += elimBdor4 [0] + " Bebedores eliminados y " + elimBdor4 [1] +" Visitas eliminadas\n";
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

    /**
     * Demostraci�n de la consulta: Para cada ciudad, cu�ntos bebedores vistan bares
     * Incluye el manejo de bares
     * Incluye el manejo de la relaci�n visitan
     * Muestra la traza de la ejecuci�n en el panelDatos
     * 
     * Pre: La base de datos est� vac�a
     * Post: La base de datos est� vac�a
     */
    public void demoBebedoresDeCiudad ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos1", "Bogot�", "Bajo", 2);
			VOBar bar2 = parranderos.adicionarBar ("Los Amigos2", "Bogot�", "Bajo", 3);
			VOBar bar3 = parranderos.adicionarBar ("Los Amigos3", "Bogot�", "Bajo", 4);
			VOBar bar4 = parranderos.adicionarBar ("Los Amigos4", "Medell�n", "Bajo", 5);
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogot�", "Alto");
			VOBebedor bdor2 = parranderos.adicionarBebedor ("Juanito", "Medell�n", "Alto");
			VOBebedor bdor3 = parranderos.adicionarBebedor ("Pedrito", "Medell�n", "Alto");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar2.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar3.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor2.getId (), bar3.getId (), new Timestamp (System.currentTimeMillis()), "diurno");
			parranderos.adicionarVisitan (bdor1.getId (), bar4.getId (), new Timestamp (System.currentTimeMillis()), "diurno");

			List<VOBar> bares = parranderos.darVOBares();
			List<VOBebedor> bebedores = parranderos.darVOBebedores();
			List<VOVisitan> visitan = parranderos.darVOVisitan ();
			long bebedoresBogota = parranderos.darCantidadBebedoresCiudadVisitanBares ("Bogot�");
			long bebedoresMedellin = parranderos.darCantidadBebedoresCiudadVisitanBares ("Medell�n");

			long [] elimBdor1 = parranderos.eliminarBebedorYVisitas_v1 (bdor1.getId ());
			long [] elimBdor2 = parranderos.eliminarBebedorYVisitas_v1 (bdor2.getId ());
			long elimBdor3 = parranderos.eliminarBebedorPorId (bdor3.getId ());
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos1");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos2");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos3");
			baresEliminados += parranderos.eliminarBarPorNombre ("Los Amigos4");

			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de dar cantidad de bebedores de una ciudad que vistan baresn\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "\n" + listarBares (bares);
			resultado += "\n" + listarBebedores (bebedores);
			resultado += "\n" + listarVisitan (visitan);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\nBebedores de Bogot�: " + bebedoresBogota;
			resultado += "\nBebedores de Medell�n: " + bebedoresMedellin;
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += elimBdor1 [0] + " Bebedores eliminados y " + elimBdor1 [1] +" Visitas eliminadas\n";
			resultado += elimBdor2 [0] + " Bebedores eliminados y " + elimBdor2 [1] +" Visitas eliminadas\n";
			resultado += elimBdor3 + " Bebedores eliminados\n";
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
	 * 			Demos de Gustan
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
    public void demoGustan ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida ("Vino tinto");
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre ("Vino tinto");
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida ("120", tipoBebida.getId (), 10);
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogot�", "Alto");
			parranderos.adicionarGustan (bdor1.getId (), bebida1.getId ());

			List <VOTipoBebida> listaTiposBebida = parranderos.darVOTiposBebida ();
			List <VOBebida> listaBebidas = parranderos.darVOBebidas ();
			List <VOBebedor> listaBebedores = parranderos.darVOBebedores ();
			List <VOGustan> listaGustan = parranderos.darVOGustan();
			long gustanEliminados = parranderos.eliminarGustan (bdor1.getId (), bebida1.getId ());
			long bebidasEliminadas = parranderos.eliminarBebidaPorNombre ("120");
			long tbEliminados = parranderos.eliminarTipoBebidaPorNombre ("Vino tinto");
			long bebedoresEliminados = parranderos.eliminarBebedorPorNombre ("Pepito");
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Gustan\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado += "\n" + listarTiposBebida (listaTiposBebida);
			resultado += "\n" + listarBebidas (listaBebidas);
			resultado += "\n" + listarBebedores (listaBebedores);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarGustan (listaGustan);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += gustanEliminados + " Gustan eliminados\n";
			resultado += bebidasEliminadas + " Bebidas eliminadas\n";
			resultado += tbEliminados + " Tipos de bebida eliminados\n";
			resultado += bebedoresEliminados + " Bebedores eliminados\n";
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
	 * 			Demos de Sirven
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
    public void demoSirven ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			boolean errorTipoBebida = false;
			VOTipoBebida tipoBebida = parranderos.adicionarTipoBebida ("Vino tinto");
			if (tipoBebida == null)
			{
				tipoBebida = parranderos.darTipoBebidaPorNombre ("Vino tinto");
				errorTipoBebida = true;
			}
			VOBebida bebida1 = parranderos.adicionarBebida ("120", tipoBebida.getId (), 10);
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos1", "Bogot�", "Bajo", 2);
			parranderos.adicionarSirven (bar1.getId (), bebida1.getId (), "diurno");

			List <VOTipoBebida> listaTiposBebida = parranderos.darVOTiposBebida ();
			List <VOBebida> listaBebidas = parranderos.darVOBebidas ();
			List <VOBar> listaBares = parranderos.darVOBares ();
			List <VOSirven> listaSirven = parranderos.darVOSirven();
			
			long sirvenEliminados = parranderos.eliminarSirven (bar1.getId (), bebida1.getId ());
			long bebidasEliminadas = parranderos.eliminarBebidaPorNombre ("120");
			long tbEliminados = parranderos.eliminarTipoBebidaPorNombre ("Vino tinto");
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos1");
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Sirven\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			if (errorTipoBebida)
			{
				resultado += "*** Exception creando tipo de bebida !!\n";
				resultado += "*** Es probable que ese tipo de bebida ya existiera y hay restricci�n de UNICIDAD sobre el nombre del tipo de bebida\n";
				resultado += "*** Revise el log de parranderos para m�s detalles\n";
			}
			resultado += "\n" + listarTiposBebida (listaTiposBebida);
			resultado += "\n" + listarBebidas (listaBebidas);
			resultado += "\n" + listarBares (listaBares);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarSirven (listaSirven);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += sirvenEliminados + " Sirven eliminados\n";
			resultado += bebidasEliminadas + " Bebidas eliminadas\n";
			resultado += tbEliminados + " Tipos de bebida eliminados\n";
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
	 * 			Demos de Visitan
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
    public void demoVisitan ( )
    {
		try 
		{
    		// Ejecuci�n de la demo y recolecci�n de los resultados
			// ATENCI�N: En una aplicaci�n real, los datos JAM�S est�n en el c�digo
			VOBar bar1 = parranderos.adicionarBar ("Los Amigos1", "Bogot�", "Bajo", 2);
			VOBebedor bdor1 = parranderos.adicionarBebedor ("Pepito", "Bogot�", "Alto");
			parranderos.adicionarVisitan (bdor1.getId (), bar1.getId (), new Timestamp (System.currentTimeMillis()), "diurno");

			List <VOBar> listaBares = parranderos.darVOBares ();
			List <VOBebedor> listaBebedores = parranderos.darVOBebedores ();
			List <VOVisitan> listaVisitan = parranderos.darVOVisitan();
			long visitanEliminados = parranderos.eliminarVisitan (bdor1.getId (), bar1.getId ());
			long baresEliminados = parranderos.eliminarBarPorNombre ("Los Amigos1");
			long bebedoresEliminadas = parranderos.eliminarBebedorPorNombre ("Pepito");
			
			// Generaci�n de la cadena de caracteres con la traza de la ejecuci�n de la demo
			String resultado = "Demo de creaci�n y listado de Visitan\n\n";
			resultado += "\n\n************ Generando datos de prueba ************ \n";
			resultado += "\n" + listarBares (listaBares);
			resultado += "\n" + listarBebedores (listaBebedores);
			resultado += "\n\n************ Ejecutando la demo ************ \n";
			resultado += "\n" + listarVisitan (listaVisitan);
			resultado += "\n\n************ Limpiando la base de datos ************ \n";
			resultado += visitanEliminados + " Visitan eliminados\n";
			resultado += bebedoresEliminadas + " Bebedores eliminadas\n";
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
    private String listarTiposBebida(List<VOTipoBebida> lista) 
    {
    	String resp = "Los tipos de bebida existentes son:\n";
    	int i = 1;
        for (VOTipoBebida tb : lista)
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
    private String listarBebidas (List<VOBebida> lista) 
    {
    	String resp = "Las bebidas existentes son:\n";
    	int i = 1;
        for (VOBebida beb : lista)
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
    private String listarBebedores (List<VOBebedor> lista) 
    {
    	String resp = "Los bebedores existentes son:\n";
    	int i = 1;
        for (VOBebedor bdor : lista)
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
    private String listarBares (List<VOBar> lista) 
    {
    	String resp = "Los bares existentes son:\n";
    	int i = 1;
        for (VOBar bar : lista)
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
    private String listarGustan (List<VOGustan> lista) 
    {
    	String resp = "Los gustan existentes son:\n";
    	int i = 1;
        for (VOGustan serv : lista)
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
    private String listarSirven (List<VOSirven> lista) 
    {
    	String resp = "Los sirven existentes son:\n";
    	int i = 1;
        for (VOSirven serv : lista)
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
    private String listarVisitan (List<VOVisitan> lista) 
    {
    	String resp = "Los visitan existentes son:\n";
    	int i = 1;
        for (VOVisitan vis : lista)
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
    private String listarBaresYBebidas (List<long[]> lista) 
    {
    	String resp = "Los bares y el n�mero de bebidas que sirven son:\n";
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
     * Genera una cadena de caracteres con la lista de parejas de objetos recibida: una l�nea por cada pareja
     * @param lista - La lista con las parejas (Bebedor, numero visitas)
     * @return La cadena con una l�nea para cada pareja recibido
     */
    private String listarBebedorYNumVisitas (List<Object[]> lista) 
    {
    	String resp = "Los bebedores y el n�mero visitas realizadas son:\n";
    	int i = 1;
        for (Object [] tupla : lista)
        {
			VOBebedor bdor = (VOBebedor) tupla [0];
			int numVisitas = (int) tupla [1];
	        String resp1 = i++ + ". " + "[";
			resp1 += bdor + ", ";
			resp1 += "numVisitas: " + numVisitas;
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
