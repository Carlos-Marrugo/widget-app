package io.ionic.starter;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GlideRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private JSONArray entries;
    private String imageUrl;

    public GlideRemoteViewsFactory(Context context, Intent intent) {
        this.context = context;
        this.imageUrl = intent.getStringExtra("imageUrl");

        try {
            String widgetData = context.getSharedPreferences("CAPACITOR_STORAGE", Context.MODE_PRIVATE)
                .getString("widget_data", "[]");
            this.entries = new JSONArray(widgetData);
        } catch (JSONException e) {
            e.printStackTrace();
            this.entries = new JSONArray();
        }
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_image_item);

        try {
            JSONObject entry = entries.getJSONObject(position);
            AppWidgetTarget widgetTarget = new AppWidgetTarget(context, R.id.widget_image_item, views);

            Glide.with(context.getApplicationContext())
                .asBitmap()
                .load(entry.getString("imageUrl"))
                .into(widgetTarget);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return views;
    }

    @Override
    public int getCount() {
        return entries.length();
    }

    @Override public void onCreate() {}
    @Override public void onDataSetChanged() {}
    @Override public void onDestroy() {}
    @Override public RemoteViews getLoadingView() { return null; }
    @Override public int getViewTypeCount() { return 1; }
    @Override public long getItemId(int position) { return position; }
    @Override public boolean hasStableIds() { return true; }
}
