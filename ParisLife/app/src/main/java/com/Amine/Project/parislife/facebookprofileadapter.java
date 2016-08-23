package com.Amine.Project.parislife;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.facebook.login.widget.ProfilePictureView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by user on 15/12/2015.
 */
public class facebookprofileadapter extends ArrayAdapter {







        Context context;
        int layoutResourceId;
        ArrayList<HashMap<String,String>> profile =new ArrayList<>();

        public facebookprofileadapter(Context context, int layoutResourceId, ArrayList<HashMap<String,String>> evennements) {

            super(context, layoutResourceId, evennements);


            this.layoutResourceId = layoutResourceId;
            this.context = context;
            this.profile = evennements;
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
                holder.Namep = (TextView) row.findViewById(R.id.nomprofile);
                holder.imagep = (ProfilePictureView) row.findViewById(R.id.imagefbprofile);
                row.setTag(holder);
            } else {
                holder = (UserHolder) row.getTag();
            }

            holder.Namep.setText(profile.get(position).get("name"));
holder.imagep.setProfileId(profile.get(position).get("id"));



            return row;
        }

        static class UserHolder {

            TextView Namep;
            ProfilePictureView imagep;
        }




    }
