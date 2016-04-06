package com.sogou.notificationui;

import android.app.Activity;
import android.content.ContentResolver;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.widget.TextView;

public class MainActivity extends Activity {

    private TextView mTextView;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTextView = (TextView) findViewById(R.id.text_view);

        boolean isNotificationEnabled = isNotificationEnabled();
        mTextView.setTextColor(isNotificationEnabled ? 0xFF009588 : Color.RED);
        mTextView.setText(isNotificationEnabled ? "接收通知已打开" : "接收通知未打开");
    }

    private boolean isNotificationEnabled() {
        ContentResolver contentResolver = getContentResolver();
        String enabledListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");

        if (!TextUtils.isEmpty(enabledListeners)) {
            return enabledListeners.contains(getPackageName() + "/" + getPackageName() + ".NotificationService");
        } else {
            return false;
        }
    }
}
