package com.example.paris.knowledge_traveller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;



public class JSONParserMap {

    private static final String TAG = "JSONParser";

    private ArrayList<Places> places;

    public JSONParserMap(){
        places = new ArrayList<>();
    }

    public ArrayList<Places> getPosts() {
        return places;
    }

    public boolean parse(String jsonData){
        try {

            final String OM_ELEMENT = "elements";
            final String OM_TAG = "tags";
            final String NAME = "name:en";
            final String other_Name = "name";

            ArrayList<Places> resultStrings = new ArrayList<>();

            JSONObject Json = new JSONObject(jsonData);
            JSONArray PArray = Json.getJSONArray(OM_ELEMENT);

            for(int i=0; i<PArray.length(); i++) {
                JSONObject AllPlaces = PArray.getJSONObject(i);

                JSONObject Place = AllPlaces.getJSONObject(OM_TAG);
                String Name = Place.getString(NAME);

                //In English historical monuments api doesnt give name:en
                if(Name==""){
                    Name= Place.getString(other_Name);
                }

                Places p = new Places();
                p.setName(Name);
                places.add(p);
            }


        } catch (JSONException e) {
            Log.e(TAG, "parse: Error parsing json data", e);
            return false;
        }
        return true;
    }
}
