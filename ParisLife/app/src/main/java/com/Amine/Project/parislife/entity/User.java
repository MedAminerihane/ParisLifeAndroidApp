package com.Amine.Project.parislife.entity;

import java.io.Serializable;


public class User implements Serializable {
    String nom, prenom, email ;
    int num;


    public User() {

    }

    public User(String nom, String prenom, String email, int num) {
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
        this.num = num;
    }


    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
