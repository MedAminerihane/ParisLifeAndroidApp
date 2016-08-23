package com.Amine.Project.parislife.entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;




@ParseClassName("Evennement")
public class Evennement extends ParseObject {

    String idEvent, lieu, adresse, nom, description, lat, lon, accees, image;
    List<DateEvent> lDate;
    List<Categorie> lRubrique;


    public Evennement() {

    }


    public Evennement(String idEvent, String adresse, String nom, String description, String lat, String lon, String accees, String image, List<DateEvent> lDate, List<Categorie> lRubrique, String lieu) {
        this.idEvent = idEvent;
        this.adresse = adresse;
        this.nom = nom;
        this.description = description;
        this.lat = lat;
        this.lon = lon;
        this.accees = accees;
        this.image = image;
        this.lDate = lDate;
        this.lRubrique = lRubrique;
        this.lieu = lieu;
    }

    public String getDescription() {

        return getString("description");
    }

    public void setDescription(String description) {
        put("description", description);
    }

    public String getLat() {

        return getString("lat");
    }

    public void setLat(String lat) {

        put("lat", lat);
    }

    public String getNom() {
        return getString("nom");
    }

    public void setNom(String nom) {
        put("nom", nom);
    }

    public String getLon() {
        return getString("lon");

    }

    public void setLon(String lon) {
        put("lon", lon);

    }


    public String getLieu() {
        return getString("lieu");
    }

    public void setLieu(String lieu) {
        put("lieu", lieu);
    }

    public String getAdresse() {
        return getString("adresse");
    }

    public void setAdresse(String adresse) {
        put("adresse", adresse);
    }


    public String getAccees() {
        return getString("accees");
    }

    public void setAccees(String accees) {
        put("accees", accees);
    }

    public String getIdEvent() {
        return getString("idEvent");
    }

    public void setIdEvent(String idEvent) {
        put("idEvent", idEvent);
    }

    public String getImage() {
        return getString("image");
    }

    public void setImage(String image) {
        put("image", image);
    }

    public List<DateEvent> getlDate() {
        return getList("lDate");
    }

    public void setlDate(List<DateEvent> lDate) {
        put("lDate", lDate);
    }

    public List<Categorie> getlRubrique() {
        return getList("lRubrique");
    }

    public void setlRubrique(List<Categorie> lRubrique) {
        put("lRubrique", lRubrique);
    }


    @Override
    public String toString() {
        return "Evennement{" +
                "idEvent='" + this.getIdEvent() + '\'' +
                ", adresse='" + this.getAdresse() + '\'' +
                ", nom='" + this.getNom() + '\'' +
                ", description='" + this.getDescription() + '\'' +
                ", accees='" + this.getAccees() + '\'' +
                ", lieu='" + this.getLieu() + '\'' +
                '}';
    }



  /*  @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(idEvent);
        dest.writeString(lieu);
        dest.writeString(adresse);
        dest.writeString(nom);
        dest.writeString(description);
        dest.writeString(lat);
        dest.writeString(lon);
        dest.writeString(accees);
        dest.writeString(image);
        //dest.writeList(lRubrique);
        //dest.writeList(lDate);

        dest.writeTypedList(lDate);
        dest.writeTypedList(lRubrique);
    }

    public static final Parcelable.Creator<Evennement> CREATOR = new Parcelable.Creator<Evennement>() {
        @Override
        public Evennement createFromParcel(Parcel source) {
            return new Evennement(source);
        }

        @Override
        public Evennement[] newArray(int size) {
            return new Evennement[size];
        }
    };

    public Evennement(Parcel in) {
        idEvent = in.readString();
        lieu = in.readString();
        adresse = in.readString();
        nom = in.readString();
        description = in.readString();
        lat = in.readString();
        lon = in.readString();
        accees = in.readString();
        image = in.readString();
        // lDate = new ArrayList<DateEvent>();
        //in.readList(lDate, null);
        //lDate = new ArrayList<DateEvent>();
       // in.readList(lRubrique, null);
        lDate= new ArrayList<DateEvent>();
        in.readTypedList(lDate, DateEvent.CREATOR);
        lRubrique= new ArrayList<Categorie>();
        in.readTypedList(lRubrique, Categorie.CREATOR);

    }
    */
}
