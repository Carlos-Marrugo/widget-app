package io.ionic.starter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;

public class WidgetListActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_list);

        try {
            String widgetData = getIntent().getStringExtra("widget_data");
            JSONArray entries = new JSONArray(widgetData);
            
            ListView listView = findViewById(R.id.widget_list_view);
            MultimediaListAdapter adapter = new MultimediaListAdapter(this, entries);
            listView.setAdapter(adapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}