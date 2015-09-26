package com.mobile.oxi.xscan;



import java.util.ArrayList;

public class ProductosData {

	public static String[] placeNameArray = {"Producto1", "Producto2", "Producto3", "Producto4", "Producto5", "Producto6", "Producto7", "Producto8"};

	public static ArrayList<Productos> ListProductos() {
		ArrayList<Productos> datos = new ArrayList<>();

		for (int i = 0; i < placeNameArray.length; i++) {

			Productos productos = new Productos(777456789123L, "Producto " + i, 36000, 10, "kg", 6, "00146532");
			productos.nomProd = "Producto " + i;
			productos.precioProd = 5000;
			productos.unidPesoProd = "KG";

			datos.add(productos);
		}
		return (datos);
	}
}
