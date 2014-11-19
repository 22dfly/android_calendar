package com.example.anl2;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DayEventAdapter extends ArrayAdapter<Event> {

    private final Context context;
    private final ArrayList<Event> event;

    public DayEventAdapter(Context context, ArrayList<Event> event) {

        super(context, R.layout.day_event, event);

        this.context = context;
        this.event = event;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;

        if (row == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            row = inflater.inflate(R.layout.day_event, parent, false);
        }

        // Set data tag
        row.setTag(R.string.data_year, event.get(position).year);
        row.setTag(R.string.data_month, event.get(position).month);
        row.setTag(R.string.data_day, event.get(position).day);
        row.setTag(R.string.data_hour, event.get(position).hour);

        TextView timeView = (TextView) row.findViewById(R.id.time);
        TextView eventView = (TextView) row.findViewById(R.id.event);

        timeView.setText(event.get(position).getTime());
        eventView.setText(event.get(position).event);

        return row;
    }
}