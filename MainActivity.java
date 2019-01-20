package com.example.paris.knowledge_traveller;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import org.greenrobot.greendao.query.Query;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int REQUEST_CODE =123;
    private ArrayList<Places> places;

    private ListView placesListView;
    private TextView emptytxt;
    private ProgressBar progressBar;
    private TextView waittxt;

    private LocationManager mLocationManager;
    private LocationListener mLocationListener;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    private Button btnvisited;
    private PlacesDao placesDao;
    private Query<Places> placesQuery;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//Επειδη το κουμπι της συνδεσης στο facebook ειναι σε ολα τα activities ελεγχουμε μηπως εχει αποσυνδεθει σε καποιο προηγουμενο activity
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(!isLoggedIn){
            //και τον στελνουμε πισω στην αρχικη οθόνη
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }

        //for real gps
       getGpsLocation();

/*
        //Ψεύτικα δεδομενα γιατί το gps στην συσκευη μου αργει να ανταποκριθει
        //Kamara
        double southbbox=40.63157;
        double westbbox = 22.95026;
        double northbbox = 40.63273;
        double eastbbox = 22.95298;

        //Aristotelous
         //southbbox=40.63633-0.0006;
         //westbbox = 22.94324 - 0.0014;
         //northbbox = 40.63633 + 0.0006;
         //eastbbox = 22.94324 + 0.0014;

        //leukos
          //southbbox=40.62633-0.0006;
         //westbbox = 22.94828 - 0.0014;
         //northbbox = 40.62633 + 0.0006;
         //eastbbox = 22.94828 + 0.0014;

        //tipota
          //southbbox=40.64501-0.0006;
         //westbbox = 22.94906 - 0.0014;
         //northbbox = 40.64501 + 0.0006;
         //eastbbox = 22.94906 + 0.0014;

        //BIG BEN
          //southbbox=51.50032-0.0006;
         //westbbox = -0.126062 - 0.0014;
         //northbbox = 51.50032 + 0.0006;
         //eastbbox = -0.126062 + 0.0014;

        DownloadData downloadData = new DownloadData();
        downloadData.execute("https://www.overpass-api.de/api/interpreter?data=[out:json][timeout:25];(" +
                "node['tourism'='museum']['name'](" + southbbox +"," + westbbox + "," + northbbox + "," + eastbbox + ");"+
                "way['tourism'='museum']['name'](" + southbbox + "," + westbbox + "," + northbbox + "," + eastbbox + ");"+
                "relation['tourism'='museum']['name'](" + southbbox + "," + westbbox + "," + northbbox + "," + eastbbox + ");"+
                "node['historic']['name'](" + southbbox + "," + westbbox + "," + northbbox + "," + eastbbox + ");" +
                "way['historic']['name'](" + southbbox + "," + westbbox + "," + northbbox + "," + eastbbox + ");" +
                "relation['historic']['name'](" + southbbox + "," + westbbox + "," + northbbox + "," + eastbbox + ");)" +
                ";out;%3E;out%20skel%20qt;");
*/
        DaoSession daoSession =((GreenDao)getApplication()).getDaoSession();

        placesDao =daoSession.getPlacesDao();
        placesQuery = placesDao.queryBuilder().orderAsc(PlacesDao.Properties.Name).build();
        final List<Places> places = placesQuery.list();


        btnvisited =findViewById(R.id.view_db);
        btnvisited.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(places.size()>1){
                Intent intent = new Intent(MainActivity.this , VisitedActivity.class);
                startActivity(intent);
                }
                else{
                    Toast toast;
                    toast= Toast.makeText(MainActivity.this ,"You need to add more places" , Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });



        emptytxt = findViewById(R.id.txtEmpy);
        emptytxt.setVisibility(View.GONE);

        placesListView = findViewById(R.id.placesListView);

    }

    @Override
    public void onResume()
    {
        super.onResume();
        //Ελεγχος για να δουμε αν ειναι συνδεδεμενος ηδη ο χρηστης απο προηγουμενη εισοδο στην εφαρμογη
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        Intent intent = new Intent(this,LoginActivity.class);
        //Αν ειναι ,τοτε να συνδεθει απευθειας στην mainActivity
        if(!isLoggedIn){
            startActivity(intent);
        }

    }


    public class DownloadData extends AsyncTask<String, Void, String> {


        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            //Προσθετουμε ενα progressbar γιατι πολλες φορες τα δεδομενα του gps αργουν να ερθουν
            waittxt =findViewById(R.id.txtWait);
            progressBar = findViewById(R.id.progressBar);
            //Τα εξαφανιζουμε μολις τα δεδομενα φτασουν στην onPost
            waittxt.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            Log.d("mpike", "onPostExecute parameter is " + jsonData );
            JSONParserMap parserMap = new JSONParserMap();

            parserMap.parse(jsonData);

            places = parserMap.getPosts();

            for(int i =0; i<places.size(); i++){
                Log.d("mpike", places.get(i).toString());
                Log.d("mpike" ,"-------------------------------");
            }


            setTheAdapter(places);

        }

        @Override
        protected String doInBackground(String... strings) {

            String Data = downloadJSON(strings[0]);
            if (Data == null) {
            }
            return Data;
        }

        private String downloadJSON(String urlPath) {
            StringBuilder sb = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();


                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line = reader.readLine();
                while (line != null) {
                    sb.append(line).append("\n");
                    line = reader.readLine();
                }

                reader.close();

            } catch (MalformedURLException e) {
            } catch (IOException e) {
            }

            return sb.toString();
        }
    }


    public void setTheAdapter(final ArrayList<Places> places){

        PlacesAdapter placeAdapter = new PlacesAdapter(MainActivity.this, R.layout.places_list, places);

        placesListView.setAdapter(placeAdapter);
        //Αν δεν εχουμε δεδομενα μεσα στην λιστα places τοτε εμφανιζουμε ενα TextView που του λεει οτι δεν υπαρχουν δεδομενα
        if(places.size()==0){
            emptytxt.setVisibility(View.VISIBLE);
        }
        //Βαζουμε στην listView μας κουμπι για καθε μνημειο
        placesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Places currentplace =places.get(position);
                String monument_Name = currentplace.getName();
                String Wiki = currentplace.getWiki();
                monument_Name=monument_Name.replaceAll("\\s","_"); // Το μνημειο εχει στο ονομα του κενα
                //Για να φωναξουμε το api της wikipedia πρεπει να ειναι σε μορφη A_B_C και οχι A B C

                Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                intent.putExtra("name",monument_Name); //Περναμε στην SecondActivity το ονομα του μνημειου καιτην σελιδα της wikipedia

                intent.putExtra("wiki" , Wiki);

                startActivity(intent);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==REQUEST_CODE){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("MyGPS", "onRequestPermissionsResult: Permission Granded");
                //οταν μας δοθούν δικαιωματα καλουμε την συναρτηση για να παρουμε την τοποθεσια του χρηστη
                getGpsLocation();
            }
            else{
                Log.d("MyGPS", "onRequestPermissionsResult: Permission Denied");
            }
        }
    }

    private void getGpsLocation() {
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);


        mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

                Log.d("MyGPS", "onLocationChanged: Location");
                //Δεχομαστε το longitude και latitude απο το gps
                String longitude= String.valueOf(location.getLongitude());
                String latitude= String.valueOf(location.getLatitude());
                String latbbox;
                String longbbox;

                //Το api του OpenStreetMap δεχεται Longitude και latitude με 5 δεκαδικα ψηφια (π.χ 62.12345 ) ενω το κινητο μας δινει με 8 ψηφια στο τελος
                //Πολλες φορες το gps μου εδωσε πισω 7 ψηφια γιαυτο εκτελουμε τον παρακατω κωδικα για πιθανον λαθη
                if(longitude.length()==8 && latitude.length()==8){
                    latbbox = latitude.substring(0,latitude.length()-3);
                    longbbox = longitude.substring(0,longitude.length()-3);
                }
                else {
                    latbbox = latitude.substring(0,latitude.length()-2);
                    longbbox = longitude.substring(0,longitude.length()-2);
                }
                //Το api του OSM για να λειτουργησει χρειαζεται το λεγομενο bbox το οποιο ειναι 4 συντεταγμενες που δημιουργουν ενα τετραγωνο στον χαρτη
                double southbbox = Double.parseDouble(latbbox) - 0.0006;
                double westbbox = Double.parseDouble(longbbox) - 0.0014;
                double northbbox = Double.parseDouble(latbbox) + 0.0006;
                double eastbbox = Double.parseDouble(longbbox) + 0.0014;

                // southbbox=40.63157;
               //  westbbox = 22.95026;
                // northbbox = 40.63273;
                // eastbbox = 22.95298;

              //  southbbox=40.63633-0.0006;
               // westbbox = 22.94324 - 0.0014;
               // northbbox = 40.63633 + 0.0006;
               // eastbbox = 22.94324 + 0.0014;

                Log.d("MyGPS", "onLocationChanged: "+ latbbox +" "+longbbox);
                Log.d("MyGPS", "onLocationChanged: " +southbbox + " " +westbbox + " " +northbbox+ " " +eastbbox );

                //Εκτελουμε την ασυγχρονη μεθοδο που θα μας επιστρεψει το Json με τα μνημεια της περιοχης του bbox
                DownloadData downloadData = new DownloadData();
                downloadData.execute("https://www.overpass-api.de/api/interpreter?data=[out:json][timeout:25];(" +
                        "node['tourism'='museum']['name'](" + southbbox +"," + westbbox + "," + northbbox + "," + eastbbox + ");"+
                        "way['tourism'='museum']['name'](" + southbbox + "," + westbbox + "," + northbbox + "," + eastbbox + ");"+
                        "relation['tourism'='museum']['name'](" + southbbox + "," + westbbox + "," + northbbox + "," + eastbbox + ");"+
                        "node['historic']['name'](" + southbbox + "," + westbbox + "," + northbbox + "," + eastbbox + ");" +
                        "way['historic']['name'](" + southbbox + "," + westbbox + "," + northbbox + "," + eastbbox + ");" +
                        "relation['historic']['name'](" + southbbox + "," + westbbox + "," + northbbox + "," + eastbbox + ");)" +
                        ";out;%3E;out%20skel%20qt;");

            }


            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
                Log.d("MyGPS", "onStatusChanged: ");

            }

            @Override
            public void onProviderEnabled(String provider) {
                Log.d("MyGPS", "onProviderEnabled: ");

            }

            @Override
            public void onProviderDisabled(String provider) {
                Log.d("MyGPS", "onLocationDisable: callback");
            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,new String[] {Manifest.permission.ACCESS_FINE_LOCATION , Manifest.permission.ACCESS_COARSE_LOCATION} ,REQUEST_CODE);

            return;
        }
        //Ελεγχουμαι εαν Το gps ή το Internet ειναι ανοιχτο

        isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if(isGPSEnabled || isNetworkEnabled) {
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, mLocationListener);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, mLocationListener);
            }
            //Αν ειναι ανοιχτα και τα δυο ζηταμε ταυτοχρονα και στα δυο να μας στειλεουν τα δεδομενα(για πιο γρηγορα αποτελεσματα)
        }
        else{
            //Αν εχει κλειστο και το ιντερνετ και το gps τοτε του ζηταμε να τα ανοιξει
            Toast toast = Toast.makeText(this ,"You Need to activate GPS" , Toast.LENGTH_LONG);
            toast.show();
        }


    }

}
