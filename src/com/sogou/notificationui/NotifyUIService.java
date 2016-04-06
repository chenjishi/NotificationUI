package com.sogou.notificationui;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Created by chenjishi on 16/4/5.
 */
public class NotifyUIService extends Service {

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("test", "##started NotifyUIService!!!!");
        ((NotifyUIApplication) getApplication()).startServicesIfNeeded();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
