package com.Amine.Project.parislife;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;
import com.squareup.picasso.Picasso;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class Actu_Detail extends AppCompatActivity {

    ImageView imgp;
    TextView pname;
    private CallbackManager callbackManager;
    LoginButton login;
    ProfileTracker profileTracker;
    FacebookCallback facebookCallback;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.root)
    FrameLayout root;
    @InjectView(R.id.content_hamburger)
    View contentHamburger;
    @InjectView(R.id.actuView)
    WebView actuView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_actu__detail);
        ButterKnife.inject(this);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }


        Bundle extras = getIntent().getExtras();

        String lien= extras.getString("url");
       // actuView.getSettings().setUseWideViewPort(true);
      actuView.getSettings().setLoadWithOverviewMode(true);
        actuView.loadUrl(lien);
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);
        root.addView(guillotineMenu);

        new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)
                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

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


}
