package io.ionic.starter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MultimediaListAdapter extends ArrayAdapter<JSONObject> {
    private final Context context;

    public MultimediaListAdapter(Context context, JSONArray entries) {
        super(context, 0);
        this.context = context;

        for (int i = 0; i < entries.length(); i++) {
            try {
                add(entries.getJSONObject(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                .inflate(R.layout.list_item_widget, parent, false);
        }

        JSONObject entry = getItem(position);
        if (entry != null) {
            try {
                ImageView imageView = convertView.findViewById(R.id.list_item_image);
                TextView descriptionView = convertView.findViewById(R.id.list_item_description);
                TextView dateView = convertView.findViewById(R.id.list_item_date);

                Glide.with(context)
                    .load(entry.getString("imageUrl"))
                    .into(imageView);

                descriptionView.setText(entry.getString("description"));

                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault());
                dateView.setText(sdf.format(new Date(entry.getString("createdAt"))));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return convertView;
    }
}
