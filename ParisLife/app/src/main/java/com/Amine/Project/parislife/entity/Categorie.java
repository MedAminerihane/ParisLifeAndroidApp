package com.Amine.Project.parislife.entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;



@ParseClassName("Categorie")
public class Categorie extends ParseObject {
    int id;
    String rubrique;

    public Categorie() {


    }

    public Categorie(int id, String rubrique) {
        this.id = id;
        this.rubrique = rubrique;
    }



    public String getId() {
        return getString("id");
    }

    public void setId(int id) {
        put("id", id);
}

    public String getRubrique() {
        return getString("rubrique");
    }

    public void setRubrique(String rubrique) {
        put("rubrique", rubrique);
    }


}
