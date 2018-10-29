package superandes.negocio;

import java.util.LinkedList;

public class Estante implements VOEstante{

	
	/* *********************************************************
	 * Atributos
	 ***********************************************************/
	

	
	private long idEstante; 
	
	private int nivelAbastecimiento;
	
	private double volumen;
	
	private double peso;
	
	private String categoria;
	
	//---------------------------- Asociaciones -----------------------------//

	private String idSucursal;
	
	//---------------------------- Constructor -----------------------------//

	public Estante() {
		this.idEstante = 0;
		this.nivelAbastecimiento = 0;
		this.peso = 0;
		this.volumen = 0;
		this.categoria = "";
		this.idSucursal = "";
	}
	
	public Estante(long idEstante, int nivelAbastecimiento , double volumen, double peso, String categoria, String idSucursal)
	{
		this.nivelAbastecimiento = nivelAbastecimiento;
		this.peso = peso;
		this.volumen = volumen;
		this.categoria = categoria;
		this.idSucursal = idSucursal;
		this.idEstante = idEstante;
	}
	
	//--------------------------------- Métodos -------------------------------------//
	@Override
	public int getNivelAbastecimiento() {
		// TODO Auto-generated method stub
		return nivelAbastecimiento;
	}
	
	public void setNivelAbastecimiento(int nivel)
	{
		this.nivelAbastecimiento = nivel;
	}

	@Override
	public String getIdSucursal() {
		// TODO Auto-generated method stub
		return idSucursal;
	}
	
	public void setIdSucursal(String id)
	{
		this.idSucursal = id;
	}

	
	
	@Override
	public double getPeso() {
		// TODO Auto-generated method stub
		return peso;
	}
	
	public void setPeso( double peso)
	{
		this.peso = peso;
	}

	@Override
	public double getVolumen() {
		// TODO Auto-generated method stub
		return volumen;
	}
	
	public void setVolumen(double volumen)
	{
		this.volumen = volumen;
	}

	@Override
	public String getCategoria() {
		// TODO Auto-generated method stub
		return categoria;
	}
	
	public void setCategoria(String categoria)
	{
		this.categoria = categoria;
	}

	
	@Override 
	
	public long getIdEstante() {
		return idEstante;
	}
	
	public void setIdEstante(long id) {
		idEstante = id;
	}
	
	public String toString()
	{
		return "Estante [  nivel abastecimiento" + nivelAbastecimiento  + "id sucursal" + idSucursal + "peso" + peso + 
				" volumen" + volumen + "categoria " + categoria + "id productos ]";
	}

}
