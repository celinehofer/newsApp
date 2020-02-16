package com.example.android.newsapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.List;

public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Context context, List<News> newsList) {
        super(context, 0, newsList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //überprüft, ob ein listItemView existiert, welche wiederverwendet werden kann, welche
        // wiederverwendet werden kann. Ansonsten wird eine neue befüllt.
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.news_list_item, parent, false);
        }

        // findet die News an der gegebenen Position in der Liste
        News currentNews = getItem(position);

        //findet die View per ID und zeigt diese in der TextView
        TextView categoryItem = (TextView) listItemView.findViewById(R.id.section_item);
        categoryItem.setText(currentNews.getSection());

        //findet die View per ID und zeigt diese in der TextView
        TextView titleItem = (TextView) listItemView.findViewById(R.id.title_item);
        titleItem.setText(currentNews.getTitle());

        //findet die View per ID und zeigt diese in der TextView
        //Formatiert das Datum korrekt
        String reformatDateTime = currentNews.getDateTime().substring(0, 10);
        String[] dateArray = reformatDateTime.split("-");
        String originalDatTime = dateArray[1] + "." + dateArray[2] + "." + dateArray[0];
        TextView timeItem = (TextView) listItemView.findViewById(R.id.date_item);
        timeItem.setText(originalDatTime);

        //gibt die View am Screen zurück
        return listItemView;
    }
}
