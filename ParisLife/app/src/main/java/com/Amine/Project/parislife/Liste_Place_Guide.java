package com.Amine.Project.parislife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.Amine.Project.parislife.entity.Place;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Liste_Place_Guide extends AppCompatActivity {

    LinearLayout favoris,accueil;
    ImageView imgp;
    TextView pname;
    private CallbackManager callbackManager;
    LoginButton login;
    ProfileTracker profileTracker;
    FacebookCallback facebookCallback;
    ProgressDialog pDialog;
    ListView listView;
    ArrayList<Place> listPlace = new ArrayList<Place>();
    public static String typeguide;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.root)
    FrameLayout root;
    @InjectView(R.id.content_hamburger)
    View contentHamburger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_restaurant__guide);
        ButterKnife.inject(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        Intent gtype = getIntent();
        typeguide=gtype.getStringExtra("guidenom");


        listView=(ListView)findViewById(R.id.list_resto);
        RetrieveRestaurantTask retrieveFeedTask = new RetrieveRestaurantTask();
        retrieveFeedTask.execute();
listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getApplicationContext(),PlaceDetail_Activity.class);
        Place place = ((Place) listView.getItemAtPosition(position));

        Bundle b = new Bundle();


        b.putString("name", place.getName());
        b.putDouble("lat", place.getLat());
        b.putDouble("lng", place.getLng());
        b.putString("adresse", place.getVicinity());
        b.putString("rate", place.getRating());
        b.putString("image", place.getReference());
        b.putString("etat",place.getEtat());
       i.putExtras(b);
        startActivity(i);


    }
});


      View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);

        favoris =(LinearLayout) guillotineMenu.findViewById(R.id.mesfavoris);

        accueil =(LinearLayout) guillotineMenu.findViewById(R.id.accueil);
        root.addView(guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)

                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

        guillotineMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        accueil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent accueil = new Intent(getApplicationContext(), MainActivity.class);
                accueil.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(accueil);
            }
        });
        favoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Favorit.class);
                startActivity(i);

            }
        });

        imgp=(ImageView) guillotineMenu.findViewById(R.id.profile_image);
        pname=(TextView) guillotineMenu.findViewById(R.id.Profilename);
        LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);
        loginButton.registerCallback(callbackManager, facebookCallback);

        //Initialize the ProfileTracker and override its
        // onCurrentProfileChanged(...) method.
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile newProfile) {
                //Whenever the user profile is changed,
                //this method will be called.
                if (newProfile == null) {
                    imgp.setImageResource(R.drawable.profile);
                    pname.setText("Déconnecté");
                } else {
                    setUpImageAndInfo(newProfile);
                }
            }
        };
        profileTracker.startTracking();

    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile userProfile = Profile.getCurrentProfile();
        if (userProfile != null) {
            setUpImageAndInfo(userProfile);
        } else {
            Log.d("gg", "Profile is Null");

            imgp.setImageResource(R.drawable.profile);
            pname.setText("Déconnecté");
        }
    }


    @Override
    protected void onStop() {
        super.onStop();
        profileTracker.stopTracking();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //When the login  button is clicked and user logs in.
        // After that, onActivityResult method is called
        callbackManager.onActivityResult(requestCode, resultCode, data);

    }

    public void setUpImageAndInfo(Profile userProfile) {
        //This method will fill up the ImageView and TextView
        // that we initialized before.
        // Toast.makeText(getApplicationContext(), userProfile.getFirstName(), Toast.LENGTH_LONG).show();

        //I am using the Picasso library to download the image
        // from URL and then load it to the ImageView.
        pname.setText(userProfile.getFirstName() + " " + userProfile.getLastName());
        Picasso.with(this)
                .load("https://graph.facebook.com/" + userProfile.getId().toString() + "/picture?type=large")
                .into(imgp);
    }












    public  class RetrieveRestaurantTask extends AsyncTask<Void, Void, String> {

        private Exception exception;

        protected void onPreExecute() {
          //  pDialog.show();

        }

        protected String doInBackground(Void... urls) {
            // String email = emailText.getText().toString();
            // Do some validation here

            try {
                //   Toast.makeText(getContext(),urlJsonArry,Toast.LENGTH_SHORT).show();
                LatLng pos =((Application) getApplication()).getPosition();
                URL url = new URL("https://maps.googleapis.com/maps/api/place/nearbysearch/"
                + "json?location="
                        + pos.latitude
                        + ","
                        + pos.longitude
                        + "&radius=50000"
                        + "&types="
                        + typeguide
                        + "&key=AIzaSyBF0ZDR7QPOhG0GD2vuQOdmt9akEk7lhMM");

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

        protected void onPostExecute(String response) {
            if (response == null) {
                response = "THERE WAS AN ERROR";
            } else {
                try {


                    JSONObject reader = new JSONObject(response);
                    JSONArray data = reader.getJSONArray("results");


                    for (int i = 0; i < data.length(); i++) {

                        JSONObject place = (JSONObject) data.get(i);
                        String idplace = place.getString("id");
                        String name = place.getString("name");

                        Double lat = place.getJSONObject("geometry").getJSONObject("location").getDouble("lat");
                        Double lng = place.getJSONObject("geometry").getJSONObject("location").getDouble("lng");
                        String reference;
                        String vicinity;
                        String rating;
                        String etat;



                        if(place.has("photos")) {
                            //it has it, do appropriate processing
                            reference = place.getJSONArray("photos").getJSONObject(0).getString("photo_reference");
                        }
                        else
                        {

                            reference =null;
                        }
                        if(place.has("opening_hours")) {
                            //it has it, do appropriate processing
                         etat = place.getJSONObject("opening_hours").getString("open_now");
                        }
                        else
                        {

                            etat ="";
                        }
                        if(place.has("vicinity")) {
                            //it has it, do appropriate processing
                             vicinity = place.getString("vicinity");
                        }
                        else
                        {

                            vicinity ="";
                        }

                        if(place.has("rating")) {
                            //it has it, do appropriate processing
                            rating = String.valueOf(place.getDouble("rating"));
                        }
                        else
                        {

                            rating ="0";
                        }




                        Place place1 = new Place();
                        place1.setId(idplace);
                        place1.setName(name);
                        place1.setEtat(etat);
                        place1.setLat(lat);
                        place1.setLng(lng);
                        place1.setReference(reference);
                        place1.setRating(rating);
                        place1.setVicinity(vicinity);
                        listPlace.add(place1);

                    }
                    PlaceAdapter placeAdapter = new PlaceAdapter(getBaseContext(),R.layout.one_item_place,listPlace);

                    listView.setAdapter(placeAdapter);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
      //     pDialog.hide();

        }}





}
