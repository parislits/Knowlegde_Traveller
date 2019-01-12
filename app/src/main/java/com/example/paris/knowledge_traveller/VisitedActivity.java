package com.example.paris.knowledge_traveller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.facebook.AccessToken;

import org.greenrobot.greendao.query.Query;

import java.util.List;

public class VisitedActivity extends AppCompatActivity {

    private PlacesDao placesDao;
    private Query<Places> placesQuery;
    private ListView ListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);

        //Ελεγχος για να δουμε αν ειναι συνδεδεμενος ηδη ο χρηστης απο προηγουμενη εισοδο στην εφαρμογη
        AccessToken accessToken = AccessToken.getCurrentAccessToken();
        final boolean isLoggedIn = accessToken != null && !accessToken.isExpired();
        //Αν ειναι ,τοτε να συνδεθει απευθειας στην mainActivity
        if(!isLoggedIn){
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
        }

        DaoSession daoSession =((GreenDao)getApplication()).getDaoSession();

        placesDao =daoSession.getPlacesDao();
        placesQuery = placesDao.queryBuilder().orderAsc(PlacesDao.Properties.Name).build();

        List<Places> places = placesQuery.list();
        ListView = findViewById(R.id.visitedView);
        setTheAdapter(places);

    }
    public void setTheAdapter(final List<Places> places){

        PlacesAdapter placeAdapter = new dbAdapter(VisitedActivity.this, R.layout.visited_items, places);
        ListView.setAdapter(placeAdapter);

        ListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Places currentplace =places.get(position);
                String monument_Name = currentplace.getName();
                String Wiki = currentplace.getWiki();

                Intent intent = new Intent(VisitedActivity.this, SecondActivity.class);
                intent.putExtra("name",monument_Name); //Περναμε στην SecondActivity το ονομα του μνημειου καιτην σελιδα της wikipedia
                intent.putExtra("wiki" , Wiki);

                startActivity(intent);
            }
        });
        ListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Places place = placesQuery.list().get(position);
                Long Id = place.getId();
                placesDao.deleteByKey(Id);
                Toast toast;
                toast = Toast.makeText(VisitedActivity.this ,"You just delete " + place.getName() , Toast.LENGTH_LONG);
                toast.show();
                return false;
            }
        });

    }



}
