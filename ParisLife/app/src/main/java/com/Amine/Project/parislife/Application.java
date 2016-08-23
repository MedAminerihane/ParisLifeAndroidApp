package com.Amine.Project.parislife;

import android.graphics.Typeface;

import com.Amine.Project.parislife.entity.Categorie;
import com.Amine.Project.parislife.entity.DateEvent;
import com.Amine.Project.parislife.entity.Evennement;
import com.google.android.gms.maps.model.LatLng;
import com.parse.Parse;
import com.parse.ParseObject;

public class Application extends android.app.Application{

    public static final String TAG = Application.class.getSimpleName();
    private LatLng position;
    private static final String CANARO_EXTRA_BOLD_PATH = "fonts/canaro_extra_bold.otf";
    public static Typeface canaroExtraBold;




    private static Application mInstance;
    public Application() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        initTypeface();

        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Evennement.class);
        ParseObject.registerSubclass(DateEvent.class);
        ParseObject.registerSubclass(Categorie.class);
        Parse.initialize(this, "FuvhpF8OiijSgB7tLVrghZTDPYSi65ThKXhjEwHo", "pDsKfQAMh3vRSZs7XIqTUhI76m93WApQt27lNecJ");

    }

    public static synchronized Application getInstance() {

        return mInstance;
    }




    public LatLng getPosition() {
        return position;
    }

    public void setPosition(LatLng position) {
        this.position = position;
    }
    private void initTypeface() {
        canaroExtraBold = Typeface.createFromAsset(getAssets(), CANARO_EXTRA_BOLD_PATH);

    }
}
