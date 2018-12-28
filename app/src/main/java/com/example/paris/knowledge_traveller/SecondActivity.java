package com.example.paris.knowledge_traveller;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.AccessToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class SecondActivity extends AppCompatActivity {
    private ArrayList<Wiki> wikiText;
    private static final String TAG = "MyGps";
    private TextView wikiTxt ,wikiTxtLink;
    private  String monument_Name;
    private ImageView imageView;
    private TextView wikiTxtLink2;
    private String monument_Wiki;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        if(!isLoggedIn){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }



        wikiTxt = findViewById(R.id.txtwiki);
        wikiTxtLink = findViewById(R.id.txtWikiLink);
        wikiTxtLink2 = findViewById(R.id.txtWikiLink2);


        Intent intent = getIntent();
        monument_Name = intent.getStringExtra("name");
        monument_Wiki = intent.getStringExtra("wiki");

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

            if(!(wikiText.get(0).getWikiText()=="")) {
                Log.d(TAG, wikiText.get(0).toString());
                Log.d(TAG, "-------------------------------");
                wikiTxt.setText(wikiText.get(0).getWikiText());
                wikiTxtLink.setText("https://en.wikipedia.org/wiki/" + monument_Name);
                if(monument_Wiki!=""){
                wikiTxtLink2.setText("https://wikipedia.org/wiki/"+monument_Wiki);
                }
                else{
                    wikiTxtLink2.setVisibility(View.GONE);
                }
                // new DownloadImageTask((ImageView) findViewById(R.id.imageView1)).
                //  execute("http://java.sogeti.nl/JavaBlog/wp-content/uploads/2009/04/android_icon_256.png");

                //new DownloadImageTask((ImageView) findViewById(R.id.imageView1))
                // .execute("https://en.wikipedia.org/wiki/"+monument_Name+"#/media/"+wikiText.get(0).getImgUrl());

            }
            else{
                wikiTxt.setText("Cant find info for " + monument_Name);
                wikiTxtLink.setText("Please search on https://en.wikipedia.org/");
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