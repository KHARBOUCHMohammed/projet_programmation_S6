package com.projet.g07;

public class Localisation {
	String name;
	double longitude,latitude;
	
	/*
	 * Constructeur de la localisation
	 */
	public Localisation(String name, double longitude, double latitude) {
		this.name = name;
		this.longitude = longitude;
		this.latitude = latitude;
	}
	
	private void update(double d, double e) {
		// TODO Auto-generated method stub
    	this.longitude = d;
    	this.latitude = e;
	}
}
