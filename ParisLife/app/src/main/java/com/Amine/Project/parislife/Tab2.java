package com.Amine.Project.parislife;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.Amine.Project.parislife.entity.Categorie;
import com.Amine.Project.parislife.entity.DateEvent;
import com.Amine.Project.parislife.entity.Evennement;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class Tab2 extends Fragment  {
public static  int occur;
      String token="854a6414181ab47dcf9281f3ed0dbb755508b16a800953d7c95318526428afb4";
  public static  String cid ="0";
    String tag = "0";;
     String created = "0";
     String start = "0";
     String end = "0";
      String offset = "0";
    String limit = "20";
    Evennement evennement;
    FloatingActionButton filter ;
    FragmentManager fm = getFragmentManager();
   public Evennement e1 = new Evennement();
    EvennementAdapter adapter;



  ProgressDialog pDialog;
ProgressBar progressBar;

    ArrayList<Evennement> evento = new ArrayList<Evennement>();



    ListView listView;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab2, container, false);
    /*    pDialog = new ProgressDialog(this.getContext());
       pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);*/


filter=(FloatingActionButton)v.findViewById(R.id.filter);
        listView = (ListView) v.findViewById(R.id.lvListPrisoners);




     //   makeEventRequest();


        return v;
    }

    @Override
    public void onViewCreated(View view, final Bundle savedInstanceState) {

            RetrieveFeedTask retrieveFeedTask = new RetrieveFeedTask();
            retrieveFeedTask.execute();


        final boolean[] categories_state = new boolean[]{
                true, true, true, true, true,
                true, true, true // Olive

        };
filter.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {




        String[] categories = new String[]{
               "Concert", "Peinture", "Spectacles",
                "Projection", "Expositions", "Activités", "Soirée", "Festival"
        };
        final String[] idc = new String[]{
                "43", "8", "2",
                "17", "37", "3", "32", "4"
        };


        // Boolean array for initial selected items

        final List<String> cList = Arrays.asList(categories);

       ContextThemeWrapper ctw = new ContextThemeWrapper( getContext(), R.style.AppCompatAlertDialogStyle );
        AlertDialog.Builder builder = new AlertDialog.Builder(ctw);

        builder.setMultiChoiceItems(categories, categories_state, new DialogInterface.OnMultiChoiceClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {

                // Update the current focused item's checked status
                categories_state[which] = isChecked;

                // Get the current focused item
                String currentItem = cList.get(which);

                // Notify the current action

            }
        });
        builder.setTitle("Vos catégories préférées ?");
        // Set the positive/yes button click listener
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click positive button
cid="0";
               for (int i = 0; i < categories_state.length; i++) {
                    boolean checked = categories_state[i];
                    if (checked) {
                        cid=cid+","+idc[i];

                    }
                }


if(adapter!=null) {
    adapter.clear();
}
                RetrieveFeedTask r = new RetrieveFeedTask();
                r.execute();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // Do something when click positive button

                dialog.cancel();


            }
        });

        AlertDialog dialog = builder.create();
        // Display the alert dialog on interface
        dialog.show();


}
    }

    );


    listView.setOnItemClickListener(new AdapterView.OnItemClickListener()

    {
        @Override
        public void onItemClick (AdapterView < ? > parent, View view,int position, long id){


        Evennement event = ((Evennement) listView.getItemAtPosition(position));


        String ide, name, desc, lieu, adresse, lat, lon, access, image,heureD,heureF;
        ArrayList<String> cat = new ArrayList<String>();
        ArrayList<String> date = new ArrayList<String>();
        ide = event.getIdEvent();
        name = event.getNom();
        desc = event.getDescription();
        lieu = event.getLieu();
        adresse = event.getAdresse();
        lat = event.getLat();
        lon = event.getLon();
        access = event.getAccees();
        image = event.getImage();
            heureD=event.getlDate().get(0).getHeureD();
            heureF=event.getlDate().get(0).getHeureF();
        for (int i = 0; i < event.getlRubrique().size(); i++) {
            cat.add(event.getlRubrique().get(i).getRubrique());
        }
        for (int i = 0; i < event.getlDate().size(); i++) {
            try {
                date.add(transformdate(String.valueOf(event.getlDate().get(i).getJour())));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        Intent i = new Intent(getContext(), EvennementActivity.class);
        Bundle b = new Bundle();
        b.putStringArrayList("date", date);
        b.putStringArrayList("cat", cat);
        b.putString("name", name);
        b.putString("desc", desc);
        b.putString("adresse", adresse);
        b.putString("access", access);
        b.putString("image", image);
        b.putString("lon", lon);
        b.putString("lat", lat);
        b.putString("id", ide);
        b.putString("lieu", lieu);
            b.putString("heureD", heureD);
                i.putExtras(b);
                startActivity(i);


            }
        });
    }



    public  class RetrieveFeedTask extends AsyncTask<Void, Void, String> {
        ProgressDialog   pDialog = new ProgressDialog(getContext());
        private Exception exception;

        protected void onPreExecute() {
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
     pDialog.show();
            //responseView.setText("");
        }

        protected String doInBackground(Void... urls) {
           // String email = emailText.getText().toString();
            // Do some validation here
            if (((MainActivity)getActivity()).isOnline()) {
            try {
             //   Toast.makeText(getContext(),urlJsonArry,Toast.LENGTH_SHORT).show();

                URL url = new URL( "https://api.paris.fr/api/data/1.4/QueFaire/get_activities/?token="+token+"&cid="+cid+"&tag="+tag+"&created="+created+"&start="+start+"&end="+end+"&offset="+offset+"&limit="+limit);

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();

                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
        }
        return "false";
        }

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                if (response.equals("false")) {
                    ParseQuery<Evennement> query = ParseQuery.getQuery(Evennement.class);
                    query.include("lRubrique");
                    query.include("lDate");
                    query.fromLocalDatastore();
                    query.findInBackground(new FindCallback<Evennement>() {
                        @Override
                        public void done(List<Evennement> eventList, com.parse.ParseException e) {
                            if (e == null) {
                                adapter = new EvennementAdapter(getActivity(),
                                        R.layout.one_item, eventList);
                                listView.setAdapter(adapter);

                                //  Log.d("score", "Retrieved " + (eventList.get(0).get("idEvent")));
                            } else {
                                Toast.makeText(getContext(), "Error parse", Toast.LENGTH_LONG).show();
                            }
                        }


                    });

                    Toast.makeText(getContext(), "Veuillez Connecter a internet SVP", Toast.LENGTH_LONG).show();
                } else {
                    try {

                        JSONObject reader = new JSONObject(response);
                        JSONArray data = reader.getJSONArray("data");


                        for (int i = 0; i < data.length(); i++) {

                            JSONObject event = (JSONObject) data.get(i);
                            String idactivite = event.getString("idactivites");
                            String name = event.getString("nom");
                            String description = event.getString("small_description");
                            String lieu = event.getString("lieu");
                            String adresse = event.getString("adresse");
                            Double lat = event.getDouble("lat");
                            Double lon = event.getDouble("lon");
                            String access = event.getString("accessType");
                            JSONArray rubriques = event.getJSONArray("rubriques");
                            ArrayList<Categorie> lcat = new ArrayList<Categorie>();
                            for (int j = 0; j < rubriques.length(); j++) {
                                JSONObject catego = (JSONObject) rubriques.get(j);
                                Categorie cat = new Categorie();

                                int id = catego.getInt("id");
                                String rubq = catego.getString("rubrique");
                                cat.setId(id);
                                cat.setRubrique(rubq);

                                lcat.add(cat);

                            }
                            JSONArray files = event.getJSONArray("files");

                            JSONObject fileo = (JSONObject) files.get(0);
                            String file = fileo.getString("file");


                            JSONArray occurrences = event.getJSONArray("occurrences");
                            ArrayList<DateEvent> locc = new ArrayList<DateEvent>();
                            for (int l = 0; l < occurrences.length(); l++) {
                                JSONObject dateo = (JSONObject) occurrences.get(l);
                                DateEvent d = new DateEvent();
                                String jour = dateo.getString("jour");
                                String heureD = dateo.getString("hour_start");
                                String heureF = dateo.getString("hour_end");
                                try {

                                    Date temp = new SimpleDateFormat("yyyy-MM-dd").parse(jour);

                                    d.setJour(temp);
                                    d.setHeureD(heureD);
                                    d.setHeureF(heureF);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                locc.add(d);

                            }

                            evennement = new Evennement();
                            evennement.setIdEvent(String.valueOf(idactivite));
                            evennement.setNom(html2text(name));
                            evennement.setAccees(access);
                            evennement.setAdresse(html2text(adresse));
                            evennement.setDescription(html2text(description));
                            evennement.setLieu(html2text(lieu));
                            evennement.setImage(file);
                            evennement.setlDate(locc);
                            evennement.setlRubrique(lcat);
                            evennement.setLat(String.valueOf(lat));
                            evennement.setLon(String.valueOf(lon));


                            evento.add(evennement);

                        }
                        try {
                            ParseObject.unpinAll();
                        } catch (com.parse.ParseException e) {
                            e.printStackTrace();
                        }
                        ParseObject.pinAllInBackground("event",evento);


                        adapter = new EvennementAdapter(getActivity(),
                                R.layout.one_item, evento);
                        listView.setAdapter(adapter);
                    /**/

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                }
            }
           if (pDialog != null) {
                pDialog.dismiss();
                pDialog = null;
            }
        }}







// Check if stopId is set, and enforce uniqueness based on the stopId column.

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }



    public static String html2text(String html) {
        html= Jsoup.parse(html).text();
        html = html.replace("&Ccedil;", "Ç");
        html = html.replace("&ccedil;", "ç");
        html = html.replace("&Aacute;", "Á");
        html = html.replace("&Acirc;", "Â");
        html = html.replace("&Atilde;", "Ã");
        html = html.replace("&Eacute;", "É");
        html = html.replace("&Ecirc;", "Ê");
        html = html.replace("&Iacute;", "Í");
        html = html.replace("&Ocirc;", "Ô");
        html = html.replace("&Otilde;", "Õ");
        html = html.replace("&Oacute;", "Ó");
        html = html.replace("&Uacute;", "Ú");
        html = html.replace("&aacute;", "á");
        html = html.replace("&acirc;", "â");
        html = html.replace("&atilde;", "ã");
        html = html.replace("&eacute;", "é");
        html = html.replace("&ecirc;", "ê");
        html = html.replace("&iacute;", "í");
        html = html.replace("&ocirc;", "ô");
        html = html.replace("&otilde;", "õ");
        html = html.replace("&oacute;", "ó");
        html = html.replace("&uacute;", "ú");
        html = html.replace("&agrave;", "à");
        html = html.replace("&auml;", "ä");
        html = html.replace("&egrave;", "è");
        html = html.replace("&ugrave;", "ù");
        html = html.replace("&quot;", "'");
        html = html.replace("&#39;", "'");
        html = html.replace("&Ucirc;", "Û");
        html = html.replace("&ucirc;", "û");
        html = html.replace("&nbsp;", " ");
        html = html.replace("&Euml;", "Ë");
        html = html.replace("&euml;", "ë");
        Spanned x;
        x= Html.fromHtml(html);

        return x.toString();
    }
    public  String transformdate(String dateStr) throws ParseException {

        //String dateStr = "Mon Jun 18 00:00:00 IST 2012";
        DateFormat formatter = new SimpleDateFormat( "EEE MMM dd HH:mm:ss z yyyy", Locale.US);
        Date date = (Date)formatter.parse(dateStr);
        System.out.println(date);

        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        String day="";
        switch (cal.get(Calendar.DAY_OF_WEEK))
        {
            case 1:
                day="Dimanche";
                break;
            case 2:
                day="Lundi";
                break;
            case 3:
                day="Mardi";
                break;
            case 4:
                day="Mercredi";
                break;
            case 5:
                day="Jeudi";
                break;
            case 6:
                day="Vendredi";
                break;
            case 7:
                day="Samedi";
                break;

        }
        String formatedDate= day+" "+cal.get(Calendar.DATE)+" "+cal.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.FRANCE ) +" "+cal.get(Calendar.YEAR);

        return formatedDate;
    }


}
