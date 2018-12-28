package com.example.paris.knowledge_traveller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class JSONParserMap {

    private static final String TAG = "JSONParser";
    private String Wiki ="" ;
    String Name;
    private int i;

    private ArrayList<Places> places;

    public JSONParserMap(){
        places = new ArrayList<>();
    }

    public ArrayList<Places> getPosts() {
        return places;
    }

    public boolean parse(String jsonData) throws JSONException {
        try {

            final String OM_ELEMENT = "elements";
            final String OM_TAG = "tags";
            final String NAME = "name:en";
            final String wiki = "wikipedia";
            final String other_Name = "name";


            JSONObject Json = new JSONObject(jsonData);
            JSONArray PArray = Json.getJSONArray(OM_ELEMENT);

            for(i=0; i<PArray.length(); i++) {
                JSONObject AllPlaces = PArray.getJSONObject(i);

                JSONObject Place = AllPlaces.getJSONObject(OM_TAG);
                try {
                    Name = Place.getString(NAME);
                }
                catch (JSONException e){
                    Name = Place.getString(other_Name);
                }

                try{
                 Wiki = Place.getString(wiki);
                    Wiki = Wiki.replaceAll("\\s","_");
                }
                catch (JSONException e){
                    Log.d(TAG, "parse: There is no wikidata");
                    Wiki = "There is no wikidata" ;

                }


                Places p = new Places();
                p.setName(Name);
                p.setWiki(Wiki);
                places.add(p);
            }


        }
        catch (JSONException e){
            Log.d(TAG, "parse: error0");
        }
                /*
                catch (ClassCastException e){
                    Log.d(TAG, "parse: Error1");

                }
                catch (NullPointerException e){
                    Log.d("MyGps", "parse: Error2");

                } catch (JSONException e) {
                    Log.d("MyGps", "parse: Error3");
                    //In English or small historical monuments api doesnt give name:en
                    final String OM_ELEMENT = "elements";
                    final String OM_TAG = "tags";
                    final String other_Name = "name";


                    JSONObject Json = new JSONObject(jsonData);
                    JSONArray PArray = Json.getJSONArray(OM_ELEMENT);

                    while(i<PArray.length()) {
                        JSONObject AllPlaces = PArray.getJSONObject(i);

                        JSONObject Place = AllPlaces.getJSONObject(OM_TAG);
                        String Name = Place.getString(other_Name);


                        Places p = new Places();
                        p.setName(Name);
                        places.add(p);
                        i++;
                    }
                }
        */
        return true;
    }
}
