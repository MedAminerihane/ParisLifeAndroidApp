package com.Amine.Project.parislife;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.Amine.Project.parislife.entity.Place;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PlaceAdapter extends ArrayAdapter<Place> {


    Context context;
    private int resourceId = 0;
    private LayoutInflater inflater;



    public PlaceAdapter(Context context,  int resourceId, List<Place> places) {
        super(context, 0, places);

        this.resourceId = resourceId;
        this.context = context;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PlaceHolder holder = new PlaceHolder();

        if (row == null) {
            row = inflater.inflate(resourceId, parent, false);

            holder.name = (TextView) row.findViewById(R.id.name_place);
            holder.review = (RatingBar) row.findViewById(R.id.rating_place);
            holder.adresse = (TextView) row.findViewById(R.id.adresse_place);
            holder.dist=(TextView)row.findViewById(R.id.distance_place);
            holder.image = (ImageView) row.findViewById(R.id.thumbnail_place);
            row.setTag(holder);
        } else {
            holder = (PlaceHolder) row.getTag();
        }

        Place place = getItem(position);
        holder.name.setText(place.getName());
        holder.review.setRating(Float.valueOf(place.getRating()));
        holder.adresse.setText(place.getVicinity());
    //    holder.dist.setText(String.valueOf(place.getLat()));


        LatLng pos = ((Application) context.getApplicationContext()).getPosition();
        double distance = getDistance(pos.latitude, pos.longitude, place.getLat(), place.getLng());
        String ds = String.valueOf(Math.round(distance));
        holder.dist.setText(ds+"m");
        if(place.getReference()!=null) {
            Picasso.with(context).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + place.getReference() + "&key=AIzaSyBF0ZDR7QPOhG0GD2vuQOdmt9akEk7lhMM").into(holder.image);
        }

        return row;
    }

    public static class PlaceHolder {

        TextView name;
        RatingBar review;
        TextView adresse;
        TextView dist;
        ImageView image;
    }

    private double getDistance(double lat1, double lon1, double lat2, double lon2) {
        //code for Distance in Kilo Meter
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        dist = Math.abs(Math.round(rad2deg(Math.acos(dist)) * 60 * 1.1515 * 1.609344 * 1000));
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad / Math.PI * 180.0);
    }

}