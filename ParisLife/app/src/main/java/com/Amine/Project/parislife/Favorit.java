package com.Amine.Project.parislife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

import com.Amine.Project.parislife.entity.Categorie;
import com.Amine.Project.parislife.entity.DateEvent;
import com.Amine.Project.parislife.entity.Evennement;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import butterknife.ButterKnife;
import butterknife.InjectView;
public class Favorit extends AppCompatActivity {
    ImageView imgp;
    TextView pname;
    LoginButton login;
    LinearLayout favoris,accueil;
    private CallbackManager callbackManager;
    ProfileTracker profileTracker;
    FacebookCallback facebookCallback;
    @InjectView(R.id.listFavoris)
    ListView listView;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.root)
    FrameLayout root;
    @InjectView(R.id.content_hamburger)
    View contentHamburger;
    private static final String PREF_NAME = "RegPref";
    SharedPreferences pref;

    SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    int nbreFavoris;
    ArrayList<Evennement> evento = new ArrayList<Evennement>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_favorit);
        callbackManager = CallbackManager.Factory.create();
        ButterKnife.inject(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }

        pref = getApplication().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();


        nbreFavoris = pref.getInt("favoris", 0);

        if (nbreFavoris != 0) {
            for (int i = 1; i < nbreFavoris + 1; i++) {
                String nom = pref.getString("nom" + i, "indisponible");
                String idEvent = pref.getString("idEvent" + i, "indisponible");
                String adress = pref.getString("adress" + i, "indisponible");
                String access = pref.getString("access" + i, "indisponible");
                String image = pref.getString("image" + i, "indisponible");
                String lieu = pref.getString("lieu" + i, "indisponible");
                String desc = pref.getString("desc" + i, "indisponible");
                String lon = pref.getString("lon" + i, "indisponible");
                String lat = pref.getString("lat" + i, "indisponible");
                String categ = pref.getString("categ" + i, "indisponible");
                String dateEvent = pref.getString("dateEvent" + i, "indisponible");
                Evennement evennement = new Evennement();
                ArrayList<DateEvent> locc = new ArrayList<DateEvent>();
                ArrayList<Categorie> lcat = new ArrayList<Categorie>();
                DateEvent d = new DateEvent();
                Categorie cat = new Categorie();
                try {
                    Date temp = new SimpleDateFormat("yyyy-MM-dd").parse(dateEvent);
                    d.setJour(temp);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                locc.add(d);
                cat.setRubrique(categ);
                lcat.add(cat);
                evennement.setIdEvent(idEvent);
                evennement.setNom(nom);
                evennement.setAccees(access);
                evennement.setAdresse(adress);
                evennement.setDescription(desc);
                evennement.setLieu(lieu);
                evennement.setImage(image);
                evennement.setlDate(locc);
                evennement.setlRubrique(lcat);
                evennement.setLat(lat);
                evennement.setLon(lon);
                evento.add(evennement);

            }
            EvennementAdapter adapter = new EvennementAdapter(Favorit.this,
                    R.layout.one_item, evento);
            listView.setAdapter(adapter);

        }
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);

        login = (LoginButton) guillotineMenu.findViewById(R.id.login_button);
        favoris =(LinearLayout) guillotineMenu.findViewById(R.id.mesfavoris);

        accueil =(LinearLayout) guillotineMenu.findViewById(R.id.accueil);
        root.addView(guillotineMenu);


        final GuillotineAnimation gui = new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)

                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

        guillotineMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Evennement event = ((Evennement) listView.getItemAtPosition(position));


                String ide, name, desc, lieu, adresse, lat, lon, access, image, heureD, heureF;
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
                heureD = event.getlDate().get(0).getHeureD();
                heureF = event.getlDate().get(0).getHeureF();
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
                Intent i = new Intent(getApplicationContext(), EvennementActivity.class);
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

        imgp=(ImageView) guillotineMenu.findViewById(R.id.profile_image);
        pname=(TextView) guillotineMenu.findViewById(R.id.Profilename);
        facebookCallback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //This code will be performed when a user is
                // successfully logged in.
                Log.d("heey", "FacebookCallback was Successful");
            }

            @Override
            public void onCancel() {
                //This code will be performed when a user
                // login is cancelled.
                Log.d("hey", "FacebookCallback Cancelled");
            }

            @Override
            public void onError(FacebookException e) {
                //This code will be performed when a users login
                // attempt gets errors.
                Log.d("heyheyhey", "FacebookCallback had Errors with \n" + e);
            }
        };

        //Initialize loginButton and register the
        // CallbackManager and FacebookCallback


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
               gui.close();

            }
        });
        // LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        login.setReadPermissions(Arrays.asList("user_friends", "basic_info"));


        login.registerCallback(callbackManager, facebookCallback);

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

        pname.setText(userProfile.getFirstName()+" "+userProfile.getLastName());
        Picasso.with(this)
                .load("https://graph.facebook.com/" + userProfile.getId().toString() + "/picture?type=large")
                .into(imgp);
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
