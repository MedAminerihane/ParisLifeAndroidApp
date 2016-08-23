package com.Amine.Project.parislife;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;


public class Tab3 extends Fragment {

    ListView malistGuide;

    int[] iconss = new int[]{R.drawable.cafeguide,R.drawable.imgguide2,R.drawable.imgguide1,R.drawable.imgguide3,R.drawable.imgguide4,R.drawable.imgguide5,R.drawable.bookguide,R.drawable.bankguide};
    String[] guides = new String[] {"Café","Bars","Restaurant","Clubs","Musées","Hotels","Bibliothèques","Bank"};

    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab3,container,false);
        malistGuide = (ListView) v.findViewById(R.id.listguide);
        final ArrayList<HashMap<String,String>> listofitems = new ArrayList<HashMap<String, String>>();

        for(int i=0;i<8;i++){
            HashMap<String, String> hm = new HashMap<String,String>();
            hm.put("guidenom",guides[i]);
            hm.put("iconss", Integer.toString(iconss[i]) );
            listofitems.add(hm);
        }

        // Keys used in Hashmap
        String[] from = { "guidenom","iconss"};

        // Ids of views in listview_layout
        int[] to = { R.id.nomguide,R.id.imageguide};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter k = new SimpleAdapter(this.getContext(), listofitems, R.layout.one_itemguide, from, to);
        ContextThemeWrapper ctw = new ContextThemeWrapper( getContext(), R.style.AppCompatAlertDialogStyle );
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(ctw);

        alertDialog.setTitle("Connexion indisponible");

        // Setting Dialog Message
        alertDialog.setMessage("Veuillez connecter a internet pour avoir accès a cette rubrique");

        // Setting Icon to Dialog
        alertDialog.setIcon(R.drawable.noincon);
        alertDialog.setNegativeButton("Retour",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog,
                                        int which) {
                        // Write your code here to execute after
                        // dialog closed
                        dialog.dismiss();

                    }
                });

        alertDialog.setPositiveButton("Se connecter",
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                    }
                });


        // Display the alert dialog on interface

        malistGuide.setAdapter(k);
        malistGuide.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> a, View v, int position,
                                    long id) {


                if (!((MainActivity)getActivity()).isOnline()){
                    final AlertDialog dialog = alertDialog.create();
                  dialog.show();

                }else{
                Intent i= new Intent(getContext(), Liste_Place_Guide.class);;
                switch(position) {
                    case 0:

                        i.putExtra("guidenom","cafe");
                        startActivity(i);
                        break;
                    case 1:

                        i.putExtra("guidenom","bar");
                        startActivity(i);
                        break;
                    case 2:

                        i.putExtra("guidenom", "restaurant|food");
                        startActivity(i);

                        break;
                    case 3:

                        i.putExtra("guidenom","night_club");
                        startActivity(i);

                        break;
                    case 4:
                        i.putExtra("guidenom","museum");
                        startActivity(i);

                        break;
                    case 5: // first one of the list
                        i.putExtra("guidenom","lodging");
                        startActivity(i);
                        break;
                    case 6: // first one of the list
                        i.putExtra("guidenom","library");
                        startActivity(i);
                        break;
                    case 7: // first one of the list
                        i.putExtra("guidenom","bank");
                        startActivity(i);
                        break;
                }

            }}
        });
        return v;
    }




}
