package superandes.interfazDemo;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

public class PanelDatos extends JPanel{

	// -----------------------------------------------------------------
    // Atributos de interfaz
    // -----------------------------------------------------------------
	/**
	 * Área de texto con barras de deslizamiento
	 */
	private JTextArea textArea;

    // -----------------------------------------------------------------
    // Constructores
    // -----------------------------------------------------------------

    /**
     * Construye el panel
     * 
     */
    public PanelDatos ()
    {
        setBorder (new TitledBorder ("Traza de las demostraciones"));
        setLayout( new BorderLayout( ) );
        
        textArea = new JTextArea("Aquí sale el resultado de la ejecución de las demos");
        textArea.setEditable(false);
        add (new JScrollPane(textArea), BorderLayout.CENTER);
    }

    // -----------------------------------------------------------------
    // Métodos
    // -----------------------------------------------------------------

    /**
     * Actualiza el panel con la información recibida por parámetro.
     * @param texto El texto con el que actualiza el área
     */
    public void actualizarInterfaz (String texto)
    {
    	textArea.setText(texto);
    }
}
