package com.example.paris.knowledge_traveller;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONParserWiki {
    private static final String TAG = "JSONParser";
    private ArrayList<Wiki> wiki;

    public JSONParserWiki(){
        wiki = new ArrayList<>();
    }

    public ArrayList<Wiki> getWiki() {
        return wiki;
    }

    public boolean parse(String jsonData){
        try {

            final String WIKI_QUERY = "query";

            final String PAGE_IDS = "pageids";
            final String WIKI_PAGE = "pages";
            final String PAGE_ID ;

            final String WIKI_EXTRACT = "extract";
            final String IMAGE = "images";
            final String Title = "title";



            JSONObject Json = new JSONObject(jsonData);
            JSONObject wiki_Query = Json.getJSONObject(WIKI_QUERY);

            JSONArray WArray = wiki_Query.getJSONArray(PAGE_IDS);


            PAGE_ID = WArray.getString(0);

            Log.d(TAG, "parse: "+PAGE_ID);

            JSONObject wiki_Page = wiki_Query.getJSONObject(WIKI_PAGE);

            JSONObject wiki_id = wiki_Page.getJSONObject(PAGE_ID);
            //Getting the text from json
            String wikiText = wiki_id.getString(WIKI_EXTRACT);
            //Getting title
            String wikiTitle = wiki_id.getString(Title);
            // Getting an Image from json
            JSONArray wikiImages = wiki_id.getJSONArray(IMAGE);
            JSONObject Image =wikiImages.getJSONObject(0);
            String wImage=Image.getString(Title);
            wImage = wImage.replaceAll("\\s","_");
            wikiTitle = wikiTitle.replaceAll("\\s","_");

            Wiki w = new Wiki();
            w.setTitle(wikiTitle);
            w.setWikiText(wikiText);
            w.setImgUrl(wImage);
            wiki.add(w);
            Log.d(TAG, "parse: " + w.getImgUrl() +"  " + w.getTitle());




        } catch (JSONException e) {
            Log.d(TAG, "parse: ERROR PARSING");
            Wiki empty = new Wiki();
            empty.setWikiText("");
            wiki.add(empty);
            return true;
        }
        return true;
    }

    public boolean parseImage(String JsonData){


        return true;
    }


}