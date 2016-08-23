package com.Amine.Project.parislife.entity;


public class Place {

	public String id;

	public String name;

	public String reference;

	public String vicinity;

	public String rating;
      public String etat;

	public double lat;
	public double lng;

	public Place() {

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getEtat() {
		return etat;
	}

	public void setEtat(String etat) {
		this.etat = etat;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getReference() {
		return reference;
	}

	public void setReference(String reference) {
		this.reference = reference;
	}





	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}



	public String getVicinity() {
		return vicinity;
	}

	public void setVicinity(String vicinity) {
		this.vicinity = vicinity;
	}

	public double getLat() {
		return lat;
	}

	public void setLat(double lat) {
		this.lat = lat;
	}

	public double getLng() {
		return lng;
	}

	public void setLng(double lng) {
		this.lng = lng;
	}

	@Override
	public String toString() {
		return name + " - " + id + " - " + reference;
	}
	

	
}
