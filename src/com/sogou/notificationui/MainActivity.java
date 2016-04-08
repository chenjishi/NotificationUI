package com.sogou.notificationui;

import android.app.Activity;
import android.app.Notification;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.graphics.Color;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTextView = (TextView) findViewById(R.id.text_view);

        changeStatus();
    }

    private void changeStatus() {
        boolean isNotificationEnabled = isNotificationEnabled();
        mTextView.setTextColor(isNotificationEnabled ? 0xFF009588 : Color.RED);
        mTextView.setText(isNotificationEnabled ? "接收通知已打开" : "接收通知未打开");
    }

    private boolean isNotificationEnabled() {
        ContentResolver contentResolver = getContentResolver();
        String enabledListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");

        Log.i("test", "##listener " + enabledListeners);

        if (!TextUtils.isEmpty(enabledListeners)) {
            return enabledListeners.contains(getPackageName() + "/" + getPackageName() + ".NotificationService");
        } else {
            return false;
        }
    }

    public void onButtonClicked(View view) {
        try {
            mNotificationListener.registerAsSystemService(this, new ComponentName(getPackageName(), getClass().getCanonicalName()),
                    UserHandle.USER_ALL);
            Log.i("test", "###registered");
        } catch (RemoteException e) {
            Log.i("test", "###unable to register notification listener " + e);
        }

        changeStatus();
    }

    public void onUnRegisterClicked(View view) {
        try {
            mNotificationListener.unregisterAsSystemService();
            Log.i("test", "###unregister service");
        } catch (RemoteException e) {
            Log.i("test", "##unregister failed");
        }
    }

    private final NotificationListenerService mNotificationListener = new NotificationListenerService() {
        @Override
        public void onNotificationPosted(StatusBarNotification sbn) {
            super.onNotificationPosted(sbn);

            Notification notification = sbn.getNotification();
            if (null == notification) return;
            Bundle extras = notification.extras;
            if (null == extras) return;

            Log.i("test", "###title " + extras.getString("android.title"));
            Log.i("test", "##text " + extras.getString("android.text"));
        }

        @Override
        public void onNotificationRemoved(StatusBarNotification sbn) {
            super.onNotificationRemoved(sbn);
            Log.i("test", "##onNotificationRemoved");

        }
    };
}
