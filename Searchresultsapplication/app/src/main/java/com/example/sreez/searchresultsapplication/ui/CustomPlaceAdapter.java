package com.example.sreez.searchresultsapplication.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.example.sreez.searchresultsapplication.R;
import com.example.sreez.searchresultsapplication.model.SearchResult;

import java.util.List;

/**
 * Created by sreez on 4/30/2016.
 */
public class CustomPlaceAdapter extends ArrayAdapter {

    public List<SearchResult> searchResultList;

    public int resource;
    public Context context;


    public CustomPlaceAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        this.searchResultList = objects;
        this.context = context;
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(convertView==null){
            LayoutInflater inflater = LayoutInflater.from(this.context);
            view = inflater.inflate(R.layout.search_result_template, null);
        }

        TextView tv = (TextView) view.findViewById(R.id.itemName);
        tv.setText(searchResultList.get(position).getDescription());
        return view;
    }
}
