package com.Amine.Project.parislife;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.squareup.picasso.Picasso;
import com.yalantis.guillotine.animation.GuillotineAnimation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class EvennementActivity extends AppCompatActivity {

    public ArrayList<HashMap<String, String>> listfinalamis;


    public  ArrayList<HashMap<String, String>> viewlistparticipant;


    ImageView imgp;
    TextView pname;
    private CallbackManager callbackManager;
    ProfileTracker profileTracker;
    FacebookCallback facebookCallback;

    LinearLayout favoris,accueil;
    @InjectView(R.id.btn_participer)
    Button btn_participe;
    @InjectView(R.id.favoris)
    ImageView btn_favoris;
    @InjectView(R.id.amis)
    ImageView amis;
    @InjectView(R.id.share_event)
    ImageView btn_share;
    @InjectView(R.id.img_event)
    ImageView imgV;
    @InjectView(R.id.name_event)
    TextView nameV;
    @InjectView(R.id.categorie)
    TextView categorieV;
    @InjectView(R.id.adresseEvent)
    TextView adrV;
    @InjectView(R.id.lieuEvent)
    TextView lieuV;
    @InjectView(R.id.distanceEvent)
    TextView distV;
    @InjectView(R.id.desc_event)
    TextView descV;
    @InjectView(R.id.accees)
    TextView accessV;

    @InjectView(R.id.dateEvent)
    TextView dateV;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.root)
    FrameLayout root;
    @InjectView(R.id.content_hamburger)
    View contentHamburger;
    String categ = "";
    String dateEvent="";
   public int nbreFavoris=0;

    private static final String PREF_NAME = "RegPref";
    static SharedPreferences pref;

    static SharedPreferences.Editor editor;
    Context _context;

    int PRIVATE_MODE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        callbackManager = CallbackManager.Factory.create();
        setContentView(R.layout.activity_evennement);
        ButterKnife.inject(this);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle(null);
        }
        ContextThemeWrapper ctw = new ContextThemeWrapper( EvennementActivity.this, R.style.AppCompatAlertDialogStyle );
        final android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(ctw);



        pref = getApplication().getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();



        MapsInitializer.initialize(getApplicationContext());

       MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.map);


        Bundle b = getIntent().getExtras();
        final String nom = b.getString("name");
        final String idEvent = b.getString("id");
        final String adress = b.getString("adresse");
        final String access = b.getString("access");
        final String image = b.getString("image");
        final String lieu = b.getString("lieu");
        final String desc = b.getString("desc");
        final String lon = b.getString("lon");
        final String lat = b.getString("lat");
        String heureD = b.getString("heureD");
        ArrayList<String> cat = b.getStringArrayList("cat");
        ArrayList<String> date = b.getStringArrayList("date");
        LatLng latLng = new LatLng(Double.parseDouble(lat), Double.parseDouble(lon));
        if(Profile.getCurrentProfile()!=null) {
            getFaceBookFriends(getApplicationContext());
        }
        Picasso.with(this).load("http://filer.paris.fr/" + image)
                .into(imgV);
        nameV.setText(nom);

        for (int i = 0; i < cat.size(); i++) {
            categ = categ + "," + cat.get(i);
        }
        categ = categ.substring(1);
        categorieV.setText(categ);
        adrV.setText(adress);
        lieuV.setText(lieu);


        LatLng pos = ((Application) this.getApplication()).getPosition();
        double distance = getDistance(pos.latitude, pos.longitude, latLng.latitude, latLng.longitude);
        String ds = String.valueOf(Math.round(distance));
        distV.setText(ds + " m");
        descV.setText(desc);
        accessV.setText(access);

        if(date.size()!=0) {
            dateEvent=date.get(0) + ", " + heureD;
        }
        dateV.setText(dateEvent);
        /*mapFragment.getMap().addMarker(new MarkerOptions()
                .position(latLng)
                .alpha(0.8f)
                .snippet(ds + "m")
                .title(lieu)
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mapFragment.getMap().moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
        Circle circle = mapFragment.getMap().addCircle(new CircleOptions().center(latLng)
                .radius(50).strokeColor(0x40ff0000)
                .fillColor(0x40ff0000));
*/
        View guillotineMenu = LayoutInflater.from(this).inflate(R.layout.guillotine, null);

        favoris =(LinearLayout) guillotineMenu.findViewById(R.id.mesfavoris);

        accueil =(LinearLayout) guillotineMenu.findViewById(R.id.accueil);

        root.addView(guillotineMenu);

    final GuillotineAnimation gui=   new GuillotineAnimation.GuillotineBuilder(guillotineMenu, guillotineMenu.findViewById(R.id.guillotine_hamburger), contentHamburger)

                .setActionBarViewForAnimation(toolbar)
                .setClosedOnStart(true)
                .build();

        guillotineMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        imgp = (ImageView) guillotineMenu.findViewById(R.id.profile_image);
        pname = (TextView) guillotineMenu.findViewById(R.id.Profilename);
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


    btn_favoris.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            nbreFavoris = pref.getInt("favoris", 0);

            if (testId(nbreFavoris, idEvent)) {
                alertDialog.setTitle("Favoris");
                alertDialog.setMessage("Vous avez déja ajouté cette événement aux favoris");
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                            }
                        });

                final AlertDialog dialog = alertDialog.create();
                dialog.show();

            } else {
                nbreFavoris = nbreFavoris + 1;
                editor.putString("idEvent" + nbreFavoris, idEvent);
                editor.putString("desc" + nbreFavoris, desc);
                editor.putString("lat" + nbreFavoris, lat);
                editor.putString("lon" + nbreFavoris, lon);
                editor.putString("lieu" + nbreFavoris, lieu);
                editor.putString("nom" + nbreFavoris, nom);
                editor.putString("access" + nbreFavoris, access);
                editor.putString("adress" + nbreFavoris, adress);
                editor.putString("image" + nbreFavoris, image);
                editor.putString("datevent" + nbreFavoris, dateEvent);
                editor.putString("categ" + nbreFavoris, categ);
                editor.putInt("favoris", nbreFavoris);


                editor.commit();
                alertDialog.setTitle("Favoris");
                alertDialog.setMessage("L'événement '" + nom + "' est ajouté aux favoris");
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                            }
                        });
                final AlertDialog dialog = alertDialog.create();
                dialog.show();

            }


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
                Intent i = new Intent(getApplicationContext(), Favorit.class);
                startActivity(i);

            }
        });










        amis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Profile.getCurrentProfile() != null) {

                JSONArray data = null;



                String frienddata=pref.getString("friends", "");
              try {

                    data=new JSONArray(frienddata);
                  Toast.makeText(getApplicationContext(), String.valueOf(data.length()), Toast.LENGTH_LONG).show();

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }


                String ch;

                listfinalamis=new ArrayList<HashMap<String, String>>();
              for (int x = 0; x < data.length(); x++) {

                    JSONObject e = new JSONObject();
                    try {

                        HashMap<String, String> friend = new HashMap<>();

                        e = data.getJSONObject(x);
                        ch = e.getString("name");
                        friend.put("name", ch);
                        ch = e.getString("id");
                        friend.put("id", ch);
                        listfinalamis.add(friend);
                    }
                    catch (Exception f) {
                        f.printStackTrace();
                    }
                }







                int s=0;


                    viewlistparticipant=new ArrayList<HashMap<String, String>>();
                    for (int i=0;i<listfinalamis.size();i++){
                        ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUser");
                        query.whereEqualTo("userId", listfinalamis.get(i).get("id"));
                        query.whereEqualTo("eventId", idEvent);
                        ParseObject object= null;
                        try {
                            object = query.getFirst();





                            HashMap<String, String> ppp = new HashMap<String, String>();

                            ppp.put("id", object.getString("userId"));
                            ppp.put("name", listfinalamis.get(i).get("name"));
                            viewlistparticipant.add(s, ppp);
                            s++;
                        } catch (ParseException e) {


                            alertDialog.setTitle("Amis");
                            alertDialog.setMessage("Aucun amis participé");
                            alertDialog.setPositiveButton("OK",
                                    new DialogInterface.OnClickListener() {

                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.dismiss();

                                        }
                                    });

                            final AlertDialog dialog = alertDialog.create();
                            dialog.show();
                         //   e.printStackTrace();
                        }







                            View contentView = getLayoutInflater().inflate(R.layout.fbfriends, null);

                            DialogPlus dialogPlus = DialogPlus.newDialog(EvennementActivity.this)
                                    .setAdapter(new facebookprofileadapter(EvennementActivity.this, R.layout.one_itemfacebook, viewlistparticipant))
                                    .setCancelable(true)
                                    .setGravity(Gravity.CENTER)
                                    .setOnDismissListener(new OnDismissListener() {
                                        @Override
                                        public void onDismiss(DialogPlus dialog) {

                                        }
                                    })
                                    .setOnCancelListener(new OnCancelListener() {
                                        @Override
                                        public void onCancel(DialogPlus dialog) {

                                        }
                                    })
                                    .setOnBackPressListener(new OnBackPressListener() {
                                        @Override
                                        public void onBackPressed(DialogPlus dialogPlus) {

                                        }
                                    })
                                    .create();

                            dialogPlus.show();

                        }






                }
                else {
                    gui.open();

                    alertDialog.setTitle("Facebook");
                    alertDialog.setMessage("Veuillez connecter SVP");
                    alertDialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.dismiss();

                                }
                            });
                    final AlertDialog dialog = alertDialog.create();
                    dialog.show();

                }

            }
        });



btn_share.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {




        ShareDialog shareDialog;
        shareDialog = new ShareDialog(EvennementActivity.this);

       ShareLinkContent shareLinkContent = new ShareLinkContent.Builder()

               .setContentTitle(nom)
               .setContentDescription(desc)
               .setContentUrl(Uri.parse("http://filer.paris.fr/" + image))



               .build();

        shareDialog.show(shareLinkContent);
    }
});

    btn_participe.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {



            if (Profile.getCurrentProfile() != null) {
                ParseQuery<ParseObject> query = ParseQuery.getQuery("EventUser");
                query.whereEqualTo("userId", Profile.getCurrentProfile().getId());
                query.whereEqualTo("eventId", idEvent);
                query.findInBackground(new FindCallback<ParseObject>() {
                    @Override
                    public void done(List<ParseObject> event, ParseException e) {
                        if (e == null) {


                               if (event.size()!=0) {
                                   alertDialog.setTitle("Evénement");
                                   alertDialog.setMessage("Vous avez déjà participer a cette évenement '"+nom+"'");
                                   alertDialog.setPositiveButton("OK",
                                           new DialogInterface.OnClickListener() {

                                               @Override
                                               public void onClick(DialogInterface dialog,
                                                                   int which) {
                                                   dialog.dismiss();

                                               }
                                           });
                                   final AlertDialog dialog = alertDialog.create();
                                   dialog.show();
                                }else {
                                      String user_id = Profile.getCurrentProfile().getId();
                                      ParseObject event_user = new ParseObject("EventUser");

                                    event_user.put("userId", user_id);
                                     event_user.put("eventId", idEvent);
                                      event_user.saveInBackground();
                                   alertDialog.setTitle("Evénement");
                                   alertDialog.setMessage("Participation avec succès a l'événement '"+nom+"'");
                                   alertDialog.setPositiveButton("OK",
                                           new DialogInterface.OnClickListener() {

                                               @Override
                                               public void onClick(DialogInterface dialog,
                                                                   int which) {
                                                   dialog.dismiss();

                                               }
                                           });
                                   final AlertDialog dialog = alertDialog.create();
                                   dialog.show();;
                                }

                        } else {
                            Toast.makeText(getApplicationContext(), "exception", Toast.LENGTH_LONG).show();

                        }
                    }
                });


            } else {
                gui.open();

                alertDialog.setTitle("Evénement");
                alertDialog.setMessage("Veuillez connecter SVP");
                alertDialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();

                            }
                        });
                final AlertDialog dialog = alertDialog.create();
                dialog.show();
            }


        }

    });



    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile userProfile = Profile.getCurrentProfile();
        if (userProfile != null) {
            setUpImageAndInfo(userProfile);
            //getFaceBookFriends(getApplicationContext());
        } else {


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

public boolean testId(int n, String idEvent){

    for (int i=1;i<n+1;i++) {

        String id = pref.getString("idEvent" + i, "invalid");
        if(idEvent.equals(id))
           return true;
    }
return false;
}

    public static void getFaceBookFriends(final Context context) {


        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        GraphRequest request = GraphRequest.newMyFriendsRequest(
                accessToken,
                new GraphRequest.GraphJSONArrayCallback() {
                    @Override
                    public void onCompleted(JSONArray array, GraphResponse response) {

                        Log.v("hjhjhj",String.valueOf(response));
                        editor.putString("friends", String.valueOf(array));
                        editor.commit();


                    }
                }
        );

        request.executeAsync();



    }






}
