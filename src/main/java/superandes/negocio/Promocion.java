package superandes.negocio;

import java.util.Date;
import java.util.LinkedList;

public class Promocion implements VOPromocion {

	
	
	
	
	//---------------------------------------------------
	// Constantes
	//---------------------------------------------------
	
	public final static String DESCUENTO_PRECIO = "Descuento precio";
	
	public final static String PAGUEX_LLEVEY = "Pague x lleve y";
	
	public final static String SEGUNDAUND_DESCUENTO = "Segunda unidad con x descuento";
	
	public final static String PAGUEX_LLEVEYCANT = "Pague x cant y lleve y cant";
	
	public final static String COMBO = "Combo producto x con y";
	
	
	
	//----------------------------------------------------
	// Atributos
	//----------------------------------------------------
	
	public long idPromocion;
	
	public String tipoPromocion;
	
	public Date fechaFin;
	
	public Date fechaInicio;

	
	public Promocion() {
		
		idPromocion = 0;
		tipoPromocion = "";
		fechaInicio = new Date(0);
		fechaFin = new Date(0);
		
		
	}
	
	public Promocion(long id, String tipo, Date pFin, Date pInicio) {
		idPromocion = id;
		tipoPromocion = tipo;
		fechaInicio = pInicio;
		fechaFin = pFin;
	}
	
	public long getIdPromocion() {
		// TODO Auto-generated method stub
		return idPromocion;
	}
	
	public void setIdPromocion(long idPromocionx) {
		
		this.idPromocion = idPromocionx;
	}
	
	
	public String getTipoPromocion() {
		// TODO Auto-generated method stub
		return null;
		
	}
	
	public  void setTipoPromocion(String pTipo) {
		
		tipoPromocion = pTipo;
		
	}
	
	
	 
	public Date getFechaInicio() {
		return fechaInicio;
	}
	
	public void setFechaInicio(Date p) {
		fechaInicio = p;
	}
	
	 
	public Date getFechaFin() {
		return fechaFin;
	}
	
	public void setFechaFin(Date p) {
		fechaFin = p;
	}
	
	public String toString() {
		return "Promocion [ id =" + idPromocion +" tipoPromocion" + tipoPromocion + "]";
	}
	
	
	/*public String toStringCompleto() {
		String resp =  this.toString();
		resp += "\n --- Visitas realizadas\n";
		int i = 1;
		for (Object [] visita : visitasRealizadas)
		{
			Bar bar = (Bar) visita [0];
			Timestamp fecha = (Timestamp) visita [1];
			String horario = (String) visita [2];
			resp += i++ + ". " + "[" +bar.toString() + ", fecha= " + fecha + ", horario= " + horario + "]\n";
		}
		resp += "\n\n --- Bebidas que le gustan\n";
		i = 1;
		for (Object [] gusta : bebidasQueLeGustan)
		{
			Bebida bebida = (Bebida) gusta [0];
			String tipoBebida = (String) gusta [1];
			resp += i++ + ". " + "[" + bebida.toString() + ", Tipo Bebida= " + tipoBebida + "]\n";
		}
		return resp;
	}
	}*/
	
	
	
	
}
