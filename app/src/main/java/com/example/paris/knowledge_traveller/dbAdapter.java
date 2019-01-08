package com.example.paris.knowledge_traveller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

class dbAdapter extends PlacesAdapter {

    private final LayoutInflater inflater;
    private final int layoutResource;
    private List<Places> places;

    public dbAdapter(@NonNull Context context, int resource, @NonNull List<Places> objects) {
        super(context, resource, objects);
        inflater = LayoutInflater.from(context);
        layoutResource = resource;
        places = objects;
        }

    @Override
    public int getCount() {
        return super.getCount();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        dbAdapter.ViewHolder viewHolder;

        if(convertView == null){
            convertView = inflater.inflate(layoutResource, parent, false);
            viewHolder = new dbAdapter.ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (dbAdapter.ViewHolder)convertView.getTag();
        }


        Places place = places.get(position);
        viewHolder.visitedName.setText(place.getName() + ""); //Προσθετουμε Το ονομα του μνημειου
        if(place.getWiki()!=""){
            viewHolder.visitedWiki.setText("www.wikipedia.org/wiki/"+ place.getWiki() + ""); //Προσθετουμε την wikipedia του μνημειου που μας δινεται απο το OSM αν υπαρχει
        }
        else{
            viewHolder.visitedWiki.setText("There is no a wikipedia page" + ""); //αλλιως οτι δεν εχει
        }


        return convertView;
    }

    private class ViewHolder {
        final TextView visitedName;
        final TextView visitedWiki;

        ViewHolder(View view){
            visitedName = view.findViewById(R.id.visitedname);
            visitedWiki = view.findViewById(R.id.visitedwiki);

        }
    }

}
