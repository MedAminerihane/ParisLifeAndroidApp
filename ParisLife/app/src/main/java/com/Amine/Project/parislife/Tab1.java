package com.Amine.Project.parislife;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.Amine.Project.parislife.entity.Weather;
import com.Amine.Project.parislife.rss.RssFragment;
import com.Amine.Project.parislife.weather.JSONWeatherParser;
import com.Amine.Project.parislife.weather.WeatherHttpClient;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import org.json.JSONException;


public class Tab1 extends Fragment {

    ImageView imgw;
    TextView temp,ville;
    static  String city="";

    private static final String PREF_NAME = "RegPref";
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v =inflater.inflate(R.layout.tab1,container,false);


        imgw = (ImageView) v.findViewById(R.id.weather);

        temp = (TextView) v.findViewById(R.id.temp);



        ville = (TextView) v.findViewById(R.id.ville);
LatLng pos =((Application) getActivity().getApplication()).getPosition();
        city = "lat=" + pos.latitude + "&" + "lon=" + pos.longitude;

        pref = getActivity().getApplication().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
        return v;


    }
    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {

        //ContextThemeWrapper ctw = new ContextThemeWrapper( getContext(), R.style.AppCompatAlertDialogStyle );
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getContext());

        alertDialog.setTitle("Connexion indisponible");

        // Setting Dialog Message
        alertDialog.setMessage("Veuillez connecter a internet pour avoir accès a toutes les rubriques");

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


    addRssFragment();
        if(((MainActivity)getActivity()).isOnline()) {
    JSONWeatherTask task = new JSONWeatherTask();
    task.execute(new String[]{city});
}else{

            temp.setText("" + pref.getFloat("temperature",0) + "°C");
            ville.setText(pref.getString("city", "Paris"));
            Picasso.with(getContext()).load(pref.getString("img","null")).error(R.drawable.meteoicon).into(imgw);
            final AlertDialog dialog = alertDialog.create();
            dialog.show();
        }
    }
    private void addRssFragment() {
        FragmentManager manager = getFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        RssFragment fragment = new RssFragment();
        transaction.add(R.id.fra, fragment);
        transaction.commit();
    }

    private class JSONWeatherTask extends AsyncTask<String, Void, Weather> {

        @Override
        protected Weather doInBackground(String... params) {
            Weather weather = new Weather();
            String data = ( (new WeatherHttpClient()).getWeatherData(params[0]));

            try {
                weather = JSONWeatherParser.getWeather(data);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            return weather;

        }

        @Override
        protected void onPostExecute(Weather weather) {
            super.onPostExecute(weather);
String imgurl ="http://openweathermap.org/img/w/"+weather.currentCondition.getIcon()+".png";
            Picasso.with(getContext()).load(imgurl).error(R.drawable.meteoicon).into(imgw);

//           String country= weather.location.getCity() + "," + weather.location.getCountry();

         //   ville.setText(country);
           float temper= Math.round((weather.temperature.getTemp() - 273.15));
            temp.setText("" + temper + "°C");
            editor.putFloat("temperature", temper);
            //editor.putString("city", country);
editor.putString("img", imgurl);
            editor.commit();



        }







    }




}
