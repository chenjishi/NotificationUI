package com.sogou.notificationui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.service.notification.StatusBarNotification;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

/**
 * Created by chenjishi on 16/4/13.
 */
public class NotificationPanelService extends Service implements View.OnClickListener, NotificationRow.ActionClickListener {

    private LinearLayout mContainer;

    private WindowManager mWindowManager;

    private float mDensity;

    private WindowManager.LayoutParams mWindowParams;

    @Override
    public void onCreate() {
        super.onCreate();
        mDensity = getResources().getDisplayMetrics().density;
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mWindowParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PRIORITY_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowParams.x = 0;
        mWindowParams.y = 0;
        mWindowParams.gravity = Gravity.TOP;
    }

    @Override
    public void onClick(View v) {
        if (null != v.getTag()) {
            final StatusBarNotification sbn = (StatusBarNotification) v.getTag();
            mContainer.postDelayed(new Runnable() {
                @Override
                public void run() {
                    hideAnimation(sbn);
                }
            }, 100);
        }
    }

    public void closePanel() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        StatusBarNotification sbn = intent.getParcelableExtra("notification");

        if (null != sbn) {
            Notification notification = sbn.getNotification();
            if (null != mContainer) {
                mWindowManager.removeView(mContainer);
                mContainer = null;
            }

            if (null != notification) {
                mContainer = new LinearLayout(this);
                mContainer.setOrientation(LinearLayout.VERTICAL);

                NotificationRow row = new NotificationRow(this);
                row.setOnClickListener(this);
                row.setActionClickListener(this);
                row.setTag(sbn);
                row.setNotification(sbn);
                mContainer.setVisibility(View.INVISIBLE);

                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
                mContainer.addView(row, lp);
                mWindowManager.addView(mContainer, mWindowParams);

                mContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showAnimation();
                    }
                }, 100);
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }

    private void showAnimation() {
        mContainer.setTranslationY(-mContainer.getHeight());
        mContainer.animate().translationY(0)
                .setDuration(300)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        mContainer.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }
                });
    }

    private void hideAnimation(final StatusBarNotification sbn) {
        mContainer.animate().translationY(-mContainer.getHeight())
                .setDuration(300)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        mContainer.setVisibility(View.GONE);
                        mWindowManager.removeView(mContainer);
                        mContainer = null;

                        NotificationData.getInstance().remove(sbn.getKey());
                        try {
                            sbn.getNotification().contentIntent.send();
                            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            manager.cancel(sbn.getId());
                        } catch (PendingIntent.CanceledException e) {
                        }
                    }
                });
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onActionClicked(PendingIntent pendingIntent) {
        try {
            pendingIntent.send();
        } catch (PendingIntent.CanceledException e) {
        }

        mWindowManager.removeView(mContainer);
        mContainer = null;
    }
}
