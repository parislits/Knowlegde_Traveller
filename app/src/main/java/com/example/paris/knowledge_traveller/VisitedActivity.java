package com.example.paris.knowledge_traveller;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import org.greenrobot.greendao.query.Query;

import java.util.ArrayList;
import java.util.List;

public class VisitedActivity extends AppCompatActivity {

    private PlacesDao placesDao;
    private Query<Places> placesQuery;
    private ListView ListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visited);

        DaoSession daoSession =((GreenDao)getApplication()).getDaoSession();

        placesDao =daoSession.getPlacesDao();
        placesQuery = placesDao.queryBuilder().orderAsc(PlacesDao.Properties.Name).build();

        List<Places> places = placesQuery.list();
        Log.d("AAA", "onCreate: " + places.get(0));
        Log.d("AAA", "onCreate: " + places.get(1));
        ListView = findViewById(R.id.visitedView);
        setTheAdapter(places);

    }
    public void setTheAdapter(final List<Places> places){

        PlacesAdapter placeAdapter = new dbAdapter(VisitedActivity.this, R.layout.visited_items, places);
        ListView.setAdapter(placeAdapter);
    }



}
