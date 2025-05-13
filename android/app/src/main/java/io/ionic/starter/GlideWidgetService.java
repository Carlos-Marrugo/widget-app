package io.ionic.starter;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class GlideWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new GlideRemoteViewsFactory(this.getApplicationContext(), intent);
    }
}