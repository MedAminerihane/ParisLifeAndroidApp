package com.Amine.Project.parislife;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.Amine.Project.parislife.entity.Evennement;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class EvennementAdapter extends ArrayAdapter {


    Context context;
    int layoutResourceId;
    List<Evennement> evennements =new ArrayList<>();

    public EvennementAdapter(Context context, int layoutResourceId, List<Evennement> evennements) {

        super(context, layoutResourceId, evennements);


        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.evennements = evennements;
//Toast.makeText(getContext(),evennements.get(1).getNom().toString(),Toast.LENGTH_LONG);

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        UserHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new UserHolder();
            holder.txtName = (TextView) row.findViewById(R.id.name);
            holder.categorie = (TextView) row.findViewById(R.id.categories);
            holder.txtAddress = (TextView) row.findViewById(R.id.lieu);
            holder.description=(TextView)row.findViewById(R.id.description);
            holder.image = (ImageView) row.findViewById(R.id.thumbnail);
            row.setTag(holder);
        } else {
            holder = (UserHolder) row.getTag();
        }
String rubrique="";
      //  evennements.get(position);
        holder.txtName.setText(evennements.get(position).getNom());
        holder.txtAddress.setText(evennements.get(position).getLieu());
        for (int i=0;i<evennements.get(position).getlRubrique().size();i++)

           rubrique = rubrique+evennements.get(position).getlRubrique().get(i).getRubrique()+"/";
        holder.categorie.setText(rubrique);
        holder.description.setText(evennements.get(position).getDescription());
        Picasso.with(context).load("http://filer.paris.fr/"+evennements.get(position).getImage())
                .into(holder.image);


        return row;
    }

    static class UserHolder {

        TextView txtName;
        TextView categorie;
        TextView txtAddress;
        TextView description;
        ImageView image;
    }




}