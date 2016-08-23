package com.Amine.Project.parislife;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

public class Introguide extends AppIntro {

    // Please DO NOT override onCreate. Use init
    @Override
    public void init(Bundle savedInstanceState) {

        // Add your slide's fragments here
        // AppIntro will automatically generate the dots indicator and buttons.
        /*addSlide(first_fragment);
        addSlide(second_fragment);
        addSlide(third_fragment);
        addSlide(fourth_fragment);*/

        // Instead of fragments, you can also use our default slide
        // Just set a title, description, background and image. AppIntro will do the rest
        addSlide(AppIntroFragment.newInstance("Accueil", "Check the weather and the latest news", R.drawable.guide1,Color.parseColor("#5DFDCB"),Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF")));
        addSlide(AppIntroFragment.newInstance("Menu", "Login with social network and share activities with friends", R.drawable.guide2, Color.parseColor("#5DFDCB"),Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF")));
        addSlide(AppIntroFragment.newInstance("Events", "Be up to date with the latest events in paris", R.drawable.guide3, Color.parseColor("#5DFDCB"),Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF")));
        addSlide(AppIntroFragment.newInstance("Event Details", "Participate and get all details about events ", R.drawable.guide3, Color.parseColor("#5DFDCB"),Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF")));
        addSlide(AppIntroFragment.newInstance("Category", "find events by your favorit categories", R.drawable.guide5, Color.parseColor("#5DFDCB"),Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF")));
        addSlide(AppIntroFragment.newInstance("Guide", "Never get lost with your guide", R.drawable.guide6, Color.parseColor("#5DFDCB"),Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF")));
        addSlide(AppIntroFragment.newInstance("Guide Details", "View Guide details", R.drawable.guide7, Color.parseColor("#5DFDCB"),Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF")));

        // OPTIONAL METHODS

        // Override bar/separator color
        setBarColor(Color.parseColor("#039BE5"));
        setSeparatorColor(Color.parseColor("#2196F3"));

        // SHOW or HIDE the statusbar
        showStatusBar(true);

        // Edit the color of the nav bar on Lollipop+ devices
        setNavBarColor(Color.parseColor("#039BE5"));

        // Hide Skip/Done button
        showSkipButton(true);
        showDoneButton(true);

        // Turn vibration on and set intensity
        // NOTE: you will probably need to ask VIBRATE permisssion in Manifest
        setVibrate(true);
        setVibrateIntensity(30);

        // Animations -- use only one of the below. Using both could cause errors.
        //setFadeAnimation(); // OR
        setZoomAnimation(); // OR
       // setFlowAnimation(); // OR
        //setSlideOverAnimation(); // OR
        //setDepthAnimation(); // OR
        //setCustomTransformer(yourCustomTransformer);

        // Permissions -- takes a permission and slide number
        //askForPermissions(new String[]{Manifest.permission.CAMERA}, 3);
    }

    private void loadMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSkipPressed() {
        // Do something when users tap on Skip button.
        loadMainActivity();
    }

    @Override
    public void onNextPressed() {

    }


    @Override
    public void onDonePressed() {
        // Do something when users tap on Done button.
        finish();
    }

    @Override
    public void onSlideChanged() {

    }
}