package com.example.anl2;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;

public class NotificationActivity extends ActionBarActivity {
    private int id;
    private DatabaseAdapter databaseAdapter;

    @SuppressWarnings("static-access")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        Bundle extras = getIntent().getExtras();
        id = extras.getInt("EVENT_ID");
        databaseAdapter = new DatabaseAdapter(this);
        Event event = databaseAdapter.getEventById(id);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Event in " + event.getTime(event.hour, false));

        TextView notfication = (TextView) findViewById(R.id.notification_content);
        notfication.setText(event.event);
    }

    public void remind_me(View view) {
        closeNotification();
    }

    public void dismiss(View view) {
        databaseAdapter.dismissEvent(Integer.toString(id));
        closeNotification();
    }
    
    private void closeNotification() {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel(R.string.notification_id);
        finish();
    }

}
