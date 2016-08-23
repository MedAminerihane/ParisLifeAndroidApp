package com.Amine.Project.parislife;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class MainActivity extends AppCompatActivity {

    LinearLayout favoris,accueil;
    ImageView imgp;
    TextView pname;
    private CallbackManager callbackManager;
    LoginButton login;
    ProfileTracker profileTracker;
    FacebookCallback facebookCallback;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Actualites","Evenement","Guide"};
    int Numboftabs =3;
    private static final long RIPPLE_DURATION = 250;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.root)
    FrameLayout root;
    @InjectView(R.id.content_hamburger)
    View contentHamburger;
static LatLng pos;


    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //  Declare a new thread to do a preference check
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);

                //  If the activity has never started before...
                if (isFirstStart) {

                    //  Launch app intro
                    Intent i = new Intent(MainActivity.this, Introguide.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();


        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);



        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
 /*Intent gpsOptionsIntent = new Intent(
                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivity(gpsOptionsIntent);*/
        GPSTracker gps;

        gps = new GPSTracker(MainActivity.this);

        if (gps.canGetLocation()) {

            double lat = gps.getLatitude();
            double lng = gps.getLongitude();
            pos= new LatLng(lat,lng);


        }else{

pos= new LatLng(48.85,2.35);

        }
        ((Application) this.getApplication()).setPosition(pos);
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);

        login = (LoginButton) guillotineMenu.findViewById(R.id.login_button);

        favoris =(LinearLayout) guillotineMenu.findViewById(R.id.mesfavoris);

        accueil =(LinearLayout) guillotineMenu.findViewById(R.id.accueil);
        root.addView(guillotineMenu);
        final GuillotineAnimation gui=   new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setStartDelay(RIPPLE_DURATION)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();
        guillotineMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });


        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);

        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);


        imgp=(ImageView) guillotineMenu.findViewById(R.id.profile_image);
        pname=(TextView) guillotineMenu.findViewById(R.id.Profilename);
        //login = (LoginButton) guillotineMenu.findViewById(R.id.login_button);


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
                gui.close();
            }
        });
        favoris.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Favorit.class);
                startActivity(i);

            }
        });
           // LoginButton loginButton = (LoginButton) findViewById(R.id.login_button);

        login.setReadPermissions(Arrays.asList("user_friends","basic_info"));


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

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


}
