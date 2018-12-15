package com.example.paris.knowledge_traveller;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class PlacesAdapter  extends ArrayAdapter<Places> {

    private static final String TAG = "PostAdapter";

    private final LayoutInflater inflater;
    private final int layoutResource;
    private List<Places> places;

    public PlacesAdapter(@NonNull Context context, int resource, @NonNull List<Places> objects) {
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
        ViewHolder viewHolder;

        if(convertView == null){
            convertView = inflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder)convertView.getTag();
        }


        Places place = places.get(position);
        viewHolder.placeName.setText(place.getName() + "");


        return convertView;
    }

    private class ViewHolder {
        final TextView placeName;

        ViewHolder(View view){
            placeName = view.findViewById(R.id.txtPlaceName);

        }
    }

}
