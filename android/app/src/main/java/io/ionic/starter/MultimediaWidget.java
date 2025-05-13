package io.ionic.starter;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.widget.RemoteViews;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.AppWidgetTarget;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MultimediaWidget extends AppWidgetProvider {
    private static int currentIndex = 0;
    private static Handler handler = new Handler();
    private static Runnable updateRunnable;
    private static final int UPDATE_INTERVAL = 2000; 

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            startAutoUpdate(context, appWidgetManager, appWidgetId);
        }
    }

    static void startAutoUpdate(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        if (updateRunnable != null) {
            handler.removeCallbacks(updateRunnable);
        }

        updateRunnable = new Runnable() {
            @Override
            public void run() {
                updateWidgetContent(context, appWidgetManager, appWidgetId);
                handler.postDelayed(this, UPDATE_INTERVAL);
            }
        };
        handler.post(updateRunnable);
    }

    static void updateWidgetContent(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences("CAPACITOR_STORAGE", Context.MODE_PRIVATE);
        String widgetData = prefs.getString("widget_data", "[]");

        try {
            JSONArray entries = new JSONArray(widgetData);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            if (entries.length() > 0) {
                currentIndex = (currentIndex + 1) % entries.length();
                JSONObject entry = entries.getJSONObject(currentIndex);

                views.setTextViewText(R.id.widget_description, entry.getString("description"));

                AppWidgetTarget widgetTarget = new AppWidgetTarget(context, R.id.widget_image, views, appWidgetId);
                Glide.with(context.getApplicationContext())
                    .asBitmap()
                    .load(entry.getString("imageUrl"))
                    .into(widgetTarget);

                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("from_widget", true);
                PendingIntent pendingIntent = PendingIntent.getActivity(
                    context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                views.setOnClickPendingIntent(R.id.widget_container, pendingIntent);
            } else {
                views.setTextViewText(R.id.widget_description, "No hay registros");
                views.setImageViewResource(R.id.widget_image, R.drawable.placeholder);
            }

            appWidgetManager.updateAppWidget(appWidgetId, views);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisabled(Context context) {
        handler.removeCallbacks(updateRunnable);
    }
}