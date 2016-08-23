package com.Amine.Project.parislife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PlaceDetail_Activity extends AppCompatActivity {
    LinearLayout favoris,accueil;
    ImageView imgp;
    TextView pname;
    private CallbackManager callbackManager;
    LoginButton login;
    ProfileTracker profileTracker;
    FacebookCallback facebookCallback;
    @InjectView(R.id.etat_place)
    TextView etatplace;
    @InjectView(R.id.name_place)
    TextView name;
    @InjectView(R.id.rating_place)
    RatingBar rating;
    @InjectView(R.id.distance_place)
    TextView distanceP;
    @InjectView(R.id.vicinity)
    TextView addrR;
    @InjectView(R.id.img_resto)
    ImageView imgR;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.root)
    FrameLayout root;
    @InjectView(R.id.content_hamburger)
    View contentHamburger;

    View guillotineMenu=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_restaurant_);
        ButterKnife.inject(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
        MapsInitializer.initialize(getApplicationContext());

        MapFragment mapFragment = (MapFragment) getFragmentManager()
                .findFragmentById(R.id.map);
        Bundle b = getIntent().getExtras();
        String nom = b.getString("name");
        Double lat = b.getDouble("lat");
        Double lng = b.getDouble("lng");
        String adresse = b.getString("adresse");
        String  rate= b.getString("rate");
        String image = b.getString("image");
        String etat = b.getString("etat");
        if(etat.equals("true")){
etatplace.setText("Ouvert");
        }else if (etat.equals("false"))  {
            etatplace.setText("Fermé");

        }else{
            etatplace.setText("Fermé");

        }
rating.setRating(Float.valueOf(rate));

        addrR.setText(adresse);
        name.setText(nom);
        if(image!=null) {
            Picasso.with(this).load("https://maps.googleapis.com/maps/api/place/photo?maxwidth=600&photoreference=" + image + "&key=AIzaSyBF0ZDR7QPOhG0GD2vuQOdmt9akEk7lhMM").into(imgR);
        }

        LatLng latLng = new LatLng(lat,lng);
        LatLng pos = ((Application) this.getApplication()).getPosition();
        double distance = getDistance(pos.latitude, pos.longitude, latLng.latitude, latLng.longitude);
        String ds = String.valueOf(Math.round(distance));
        distanceP.setText(ds+ "m");
        mapFragment.getMap().addMarker(new MarkerOptions()
                .position(latLng)
                .alpha(0.8f)
                .snippet(ds+"m")
                .title(nom)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        Circle circle= mapFragment.getMap().addCircle(new CircleOptions().center(latLng)
                .radius(50).strokeColor(0x40ff0000)
                .fillColor(0x40ff0000));


            guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);

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



