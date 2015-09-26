package com.mobile.oxi.xscan;

import android.content.Context;

public class Productos {

	public String imageName;
	public boolean isFav;

	public long codBarProd;
	public String nomProd;
	public int precioProd;
	public int pesoProd;
	public String unidPesoProd;
	public int cantProd;
	public String codIntProd;

	public int getImageResourceId(Context context) {
		return context.getResources().getIdentifier(this.imageName, "drawable", context.getPackageName());
	}

	public Productos(long codBar, String nom, int precio, int peso, String uniPeso, int cant, String codInt) {

		codBarProd = codBar;
		nomProd = nom;
		precioProd = precio;
		pesoProd = peso;
		unidPesoProd = uniPeso;
		cantProd = cant;
		codIntProd = codInt;

	}

	public long getCodBarrasProd() {
		return codBarProd;
	}

	public String getNombreProd() {
		return nomProd;
	}

	public int getPrecioProd() {
		return precioProd;
	}

	public int getPesoProd() {
		return pesoProd;
	}

	public String getUniPesoProd() {
		return unidPesoProd;
	}

	public int getCantProd() {
		return cantProd;
	}

	public String getCodIntProd() {
		return codIntProd;
	}

}
