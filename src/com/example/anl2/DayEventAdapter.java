package com.example.anl2;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class DayEventAdapter extends ArrayAdapter<DayEvent> {

	private final Context context;
	private final ArrayList<DayEvent> dayEvent;

	public DayEventAdapter(Context context, ArrayList<DayEvent> dayEvent) {

		super(context, R.layout.day_event, dayEvent);

		this.context = context;
		this.dayEvent = dayEvent;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View row = convertView;
		if (row == null) {
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			row = inflater.inflate(R.layout.day_event, parent, false);
		}
		
		// Set data tag
		row.setTag(R.string.data_year, dayEvent.get(position).getYear());
		row.setTag(R.string.data_month, dayEvent.get(position).getMonth());
		row.setTag(R.string.data_day, dayEvent.get(position).getDay());
		row.setTag(R.string.data_hour, dayEvent.get(position).getHour());

		TextView time = (TextView) row.findViewById(R.id.time);
		TextView event = (TextView) row.findViewById(R.id.event);

		time.setText(dayEvent.get(position).getTime());
		event.setText(dayEvent.get(position).getEvent());

		return row;
	}
}