package io.ionic.starter;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.RemoteViews;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
  private Context context;
  private AppWidgetManager appWidgetManager;
  private int appWidgetId;
  private int imageViewId;

  public LoadImageTask(Context context, AppWidgetManager appWidgetManager,
                       int appWidgetId, int imageViewId) {
    this.context = context;
    this.appWidgetManager = appWidgetManager;
    this.appWidgetId = appWidgetId;
    this.imageViewId = imageViewId;
  }

  protected Bitmap doInBackground(String... urls) {
    try {
      URL url = new URL(urls[0]);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setDoInput(true);
      connection.connect();
      InputStream input = connection.getInputStream();
      return BitmapFactory.decodeStream(input);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  protected void onPostExecute(Bitmap result) {
    if (result != null) {
      RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
      views.setImageViewBitmap(imageViewId, result);
      appWidgetManager.updateAppWidget(appWidgetId, views);
    }
  }
}
