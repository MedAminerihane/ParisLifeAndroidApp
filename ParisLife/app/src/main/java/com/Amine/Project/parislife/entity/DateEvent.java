package com.Amine.Project.parislife.entity;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.Date;


@ParseClassName("DateEvent")
public class DateEvent extends ParseObject{
    Date jour;
    String heureD,heureF;

    public DateEvent() {
    }

    public DateEvent(Date jour, String heureD, String heureF) {
        this.jour = jour;
        this.heureD = heureD;
        this.heureF = heureF;
    }
    public Date getJour() {

        return getDate("jour");
    }

    public void setJour(Date jour) {

        put("jour", jour);
    }

    public String getHeureD() {


        return getString("heureD");
    }

    public void setHeureD(String heureD) {

        put("heureD", heureD);
    }
    public String getHeureF() {

        return getString("heureF");
    }

    public void setHeureF(String heureF) {

        put("heureF", heureF);
    }

 /*   @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
dest.writeString(heureD);
        dest.writeString(heureF);
        dest.writeLong(jour.getTime());
    }

    public static final Parcelable.Creator<DateEvent> CREATOR = new Parcelable.Creator<DateEvent>(){

        @Override
        public DateEvent createFromParcel(Parcel source) {
            return new DateEvent(source);
        }

        @Override
        public DateEvent[] newArray(int size) {
            return new DateEvent[size];
        }



    };
    public DateEvent(Parcel in) {
        heureD = in.readString();
        heureF = in.readString();
        jour= new Date(in.readLong());
    }*/
}
