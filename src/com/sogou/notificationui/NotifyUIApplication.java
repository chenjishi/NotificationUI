package com.sogou.notificationui;

import android.app.Application;
import android.content.*;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;

/**
 * Created by chenjishi on 16/3/31.
 */
public class NotifyUIApplication extends Application {

    private NotificationService mNotificationListener;

    private boolean mServiceStarted;

    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter filter = new IntentFilter(Intent.ACTION_BOOT_COMPLETED);
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("test", "####boot complete");
                unregisterReceiver(this);

            }
        }, filter);
    }

//    public void startServicesIfNeeded() {
//        if (mServiceStarted) return;
//
//        mNotificationListener = new NotificationService();
//
//        // Set up the initial notification state.
//        try {
//            mNotificationListener.registerAsSystemService(this,
//                    new ComponentName(getPackageName(), getClass().getCanonicalName()),
//                    UserHandle.USER_ALL);
//            mServiceStarted = true;
//            Log.i("test", "###register OK");
//        } catch (RemoteException e) {
//            Log.e("test", "Unable to register notification listener", e);
//        }
//
//    }

}
