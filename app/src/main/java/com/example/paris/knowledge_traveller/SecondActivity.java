package com.example.paris.knowledge_traveller;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

public class SecondActivity extends AppCompatActivity {
    private ArrayList<Wiki> wikiText;
    private static final String TAG = "SecondActivity";
    private TextView wikiTxt ,wikiTxtLink;
    private  String monument_Name;
    private TextView wikiTxtLink2;
    private String monument_Wiki;
    private PlacesDao placesDao;
    private Query<Places> placesQuery;
    private Button addPlace;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        //Επειδη το κουμπι της συνδεσης στο facebook ειναι σε ολα τα activities ελεγχουμε μηπως εχει αποσυνδεθει σε καποιο προηγουμενο activity


        DaoSession daoSession =((GreenDao)getApplication()).getDaoSession();

        placesDao =daoSession.getPlacesDao();
        placesQuery = placesDao.queryBuilder().orderAsc(PlacesDao.Properties.Name).build();
        final List<Places> places = placesQuery.list();

        addPlace = findViewById(R.id.addbtn);

        wikiTxt = findViewById(R.id.txtwiki);
        wikiTxtLink = findViewById(R.id.txtWikiLink);
        wikiTxtLink2 = findViewById(R.id.txtWikiLink2);


        Intent intent = getIntent();
        monument_Name = intent.getStringExtra("name");
        monument_Wiki = intent.getStringExtra("wiki");
        //Δεχομαστε απο τα προηγουμενα Activities το ονομα του μνημειου και την σελιδα της wikipedia Που μας δινει το OSM

        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Places p = new Places();
                p.setName(monument_Name);
                p.setWiki(monument_Wiki);
                boolean flag=true;
                //Αν υπαρχει ηδη μεσα στην βάση μην το ξαναπροσθεσεις
                for(int i=0 ;i<places.size();i++) {
                    if (places.get(i).getName().equals(p.getName())){
                        flag=false;
                    }
                }

                    if(flag) {
                        placesDao.insert(p);
                    }
                    else{

                        Toast toast;
                        toast = Toast.makeText(SecondActivity.this ,"This Monument is already added !" , Toast.LENGTH_LONG);
                        toast.show();
                    }

            }
        });
        //Κανουμε αναζητηση στη wikipedia με βαση το ονομα του μνημειου και παιρνουμε μονο την περιγραφη της wikipedia
        DownloadWikiData downloadWikiData = new DownloadWikiData();
        downloadWikiData.execute("https://en.wikipedia.org//w/api.php?action=query&format=json&prop=extracts%7Cimages&indexpageids=1&titles="+monument_Name+"&redirects=1&utf8=1&exintro=1&explaintext=1");


    }



    public class DownloadWikiData extends AsyncTask<String, Void, String> {


        @Override
        protected void onPostExecute(String jsonData) {
            super.onPostExecute(jsonData);
            Log.d(TAG, "onPostExecute: Something else");
            JSONParserWiki parserWiki =new JSONParserWiki();
            parserWiki.parse(jsonData);
            wikiText = parserWiki.getWiki();
            //Αν δεν
            if(!(wikiText.get(0).getWikiText()=="")) {
                Log.d(TAG, wikiText.get(0).toString());
                Log.d(TAG, "-------------------------------");
                wikiTxt.setText(wikiText.get(0).getWikiText());
                wikiTxtLink.setText("https://en.wikipedia.org/wiki/" + monument_Name); //αν υπαρχει η σελιδα αναζητησης απο την wikipedia την εμφανιζουμε

                if(!monument_Wiki.isEmpty()){
                wikiTxtLink2.setText("https://wikipedia.org/wiki/"+monument_Wiki);  //Αν υπαρχει η Σελιδα wikipedia απο το OSM την εμφανιζουμε και αυτη
                //Βαζουμε και τις δυο σελιδες γιατι η 2η μπορει να μην ειναι στα αγγλικα αλλα σε καποια αλλη γλωσσα
                }
                else{
                     wikiTxtLink2.setVisibility(View.GONE);
                     }

            }
            else{
                //Αν δεν υπαχρει η wiki στα αγγλικα αλλα υπαρχει σε αλλη γλωσσα
                if(!monument_Wiki.isEmpty()){
                    wikiTxtLink2.setText("https://wikipedia.org/wiki/"+monument_Wiki);
                    wikiTxtLink.setVisibility(View.GONE);
                }
                else {
                    //Δεν υπαρχουν καθολου δεδομενα για το μνημειο
                    wikiTxtLink2.setVisibility(View.GONE);
                    wikiTxt.setText("Cant find info for " + monument_Name);
                    wikiTxtLink.setText("Please search on https://en.wikipedia.org/ or https://google.com ");
                }
            }




        }

        @Override
        protected String doInBackground(String... strings) {
            String Data = downloadJSON(strings[0]);
            if (Data == null) {
                Log.e(TAG, "doInBackground: Error downloading from url " + strings[0]);
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
                Log.e(TAG, "downloadJSON: not correct URL: " + urlPath, e);
            } catch (IOException e) {
                Log.e(TAG, "downloadJSON: io error ", e);
            }

            return sb.toString();

        }


    }

}