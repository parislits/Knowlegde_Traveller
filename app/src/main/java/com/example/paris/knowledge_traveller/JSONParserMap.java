package com.example.paris.knowledge_traveller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class JSONParserMap {

    private String Wiki ="";
    String Name;

    private ArrayList<Places> places;

    public JSONParserMap(){
        places = new ArrayList<>();
    }

    public ArrayList<Places> getPosts() {
        return places;
    }

    public boolean parse(String jsonData) {
        try {

            final String OM_ELEMENT = "elements";
            final String OM_TAG = "tags";
            final String NAME = "name:en";
            final String wiki = "wikipedia";
            final String other_Name = "name";
            //Ανοιγουμε το Json με βαση τα ονοματα που εχει

            JSONObject Json = new JSONObject(jsonData);
            JSONArray PArray = Json.getJSONArray(OM_ELEMENT);

            for(int i=0; i<PArray.length(); i++) {
                JSONObject AllPlaces = PArray.getJSONObject(i);

                JSONObject Place = AllPlaces.getJSONObject(OM_TAG);
               /*Σε πολλα ελληνικα μικρα μνημεια αλλα και σε αγγλικα μνημεια δεν υπαρχει το tag "name:en" αλλα απλα name
               Σε αυτες τις περιπτωσεις θα βγαλει error οποτε το πιανουμε πανω για να μην βγει απο την for*/
                try {
                    Name = Place.getString(NAME);
                }
                catch (JSONException e){
                    Name = Place.getString(other_Name);
                }
                /* Το ιδιο ισχυει και για το πεδιο wiki
                Τα μικρα μνημεια δεν εχουν σελιδα στην wikipedia*/
                try{
                 Wiki = Place.getString(wiki);
                    Wiki = Wiki.replaceAll("\\s","_"); /*Αν εχουν σελιδα για να την περασουμε σε link για να μπορουν να την ανοιγουν
                     Θα πρεπει να αφαιρεσουμε τα κενα και να βαλουμε _ */
                }
                catch (JSONException e){
                    Wiki ="";

                }


                Places p = new Places();
                p.setName(Name);
                p.setWiki(Wiki);
                places.add(p);
            }


        }
        catch (JSONException e){
        }

        return true;
    }
}
