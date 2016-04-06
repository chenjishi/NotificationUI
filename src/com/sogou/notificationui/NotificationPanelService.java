package com.sogou.notificationui;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * Created by chenjishi on 16/3/22.
 */
public class NotificationPanelService extends Service implements View.OnClickListener {

    private WindowManager mWindowManager;

    @Override
    public void onCreate() {
        super.onCreate();

        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StatusBarNotification sbn = intent.getParcelableExtra("notification");

        final float density = getResources().getDisplayMetrics().density;

        if (null != sbn) {
            Notification notification = sbn.getNotification();

            if (null != notification) {
                Bundle extras = notification.extras;

                if (null != extras) {
                    LinearLayout layout = getNotificationPanel(extras);

                    WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams((int) (density * 200), WindowManager.LayoutParams.WRAP_CONTENT,
                            WindowManager.LayoutParams.TYPE_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                            PixelFormat.TRANSLUCENT);
                    layoutParams.x = 0;
                    layoutParams.y = 0;
                    layoutParams.gravity = Gravity.CENTER;

                    mWindowManager.addView(layout, layoutParams);
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onClick(View v) {
        mWindowManager.removeView(v);
    }

    private LinearLayout getNotificationPanel(Bundle extras) {
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setBackgroundColor(0xFF646464);
        layout.setOnClickListener(this);

        TextView titleText = new TextView(this);
        String title = extras.getString("android.title");
        Log.i("test", "#title " + title);
        if (!TextUtils.isEmpty(title)) {
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            titleText.setText(title);
            titleText.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16.f);
            titleText.setTextColor(0xFF333333);
            layout.addView(titleText, lp);
        }

        TextView textView = new TextView(this);
        String detailText = extras.getString("android.text");
        Log.i("test", "#text " + detailText);

        if (!TextUtils.isEmpty(detailText)) {
            LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            textView.setText(detailText);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14.f);
            textView.setTextColor(0xFF999999);
            layout.addView(textView, lp1);
        }

        return layout;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
