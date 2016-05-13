package com.sogou.notificationui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;
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

    private WindowManager.LayoutParams mWindowParams;

    private NotificationData mNotificationData;

    private final NotificationListenerService mNotificationListener = new NotificationListenerService() {

        @Override
        public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
            super.onNotificationPosted(sbn, rankingMap);
            addNotificationPanel(sbn);
            Log.i("test", "###onNotificationPosted ");
        }

        @Override
        public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
            super.onNotificationRemoved(sbn, rankingMap);
            mNotificationData.remove(sbn.getKey());
            Log.i("test", "###onNotificationRemoved ");
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        mWindowParams = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PRIORITY_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        mWindowParams.x = 0;
        mWindowParams.y = 0;
        mWindowParams.gravity = Gravity.TOP;

        mNotificationData = NotificationData.getInstance();

        Log.i("test", "###NotificationPanelService started!");

        try {
            mNotificationListener.registerAsSystemService(this, new ComponentName(getPackageName(), getClass().getCanonicalName()),
                    UserHandle.USER_ALL);
            Log.i("test", "##registered!!");
        } catch (RemoteException e) {
            Log.e("NotificationUI", "Unable to register notification listener " + e);
        }
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

    private void addNotificationPanel(StatusBarNotification sbn) {
        if (null == sbn) return;

        Log.i("test", "##sbn " + sbn);
        mNotificationData.add(sbn);
        Notification notification = sbn.getNotification();
        if (null != mContainer) {
            mWindowManager.removeView(mContainer);
            mContainer = null;
        }

        Log.i("test", "##notification " + notification);
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

            if (sbn.getPackageName().equals("com.sogou.carphone")) {
                showPhoneNotification();
            } else {
                mWindowManager.addView(mContainer, mWindowParams);

                mContainer.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        showAnimation();
                    }
                }, 100);
            }
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    private void showPhoneNotification() {
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PRIORITY_PHONE, WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSLUCENT);
        lp.x = 0;
        lp.y = 0;
        lp.gravity = Gravity.LEFT | Gravity.BOTTOM;
        mWindowManager.addView(mContainer, lp);
        mContainer.postDelayed(new Runnable() {
            @Override
            public void run() {
                mContainer.setTranslationX(-mContainer.getWidth());
                mContainer.animate().translationX(0)
                        .setDuration(300)
                        .setInterpolator(new LinearInterpolator())
                        .setListener(new AnimatorListenerAdapter() {
                            @Override
                            public void onAnimationStart(Animator animation) {
                                mContainer.setVisibility(View.VISIBLE);
                            }
                        });

            }
        }, 100);
    }

    private void showAnimation() {
        Log.i("test", "#showAnimation");
        mContainer.setTranslationY(-mContainer.getHeight());
        mContainer.animate().translationY(0)
                .setDuration(300)
                .setInterpolator(new LinearInterpolator())
                .setListener(new AnimatorListenerAdapter() {

                    @Override
                    public void onAnimationStart(Animator animation) {
                        mContainer.setVisibility(View.VISIBLE);
                        Log.i("test", "##onAnimationStart");
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                    }
                });
    }

    private void hideAnimation(final StatusBarNotification sbn) {
        if (sbn.equals("com.sogou.carphone")) {
            mContainer.animate().translationX(-mContainer.getWidth())
                    .setDuration(300)
                    .setInterpolator(new LinearInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animateEnd(sbn);
                        }
                    });
        } else {
            mContainer.animate().translationY(-mContainer.getHeight())
                    .setDuration(300)
                    .setInterpolator(new LinearInterpolator())
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            animateEnd(sbn);
                        }
                    });
        }
    }

    private void animateEnd(StatusBarNotification sbn) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotificationData.clear();
        try {
            mNotificationListener.unregisterAsSystemService();
        } catch (RemoteException e) {
        }
    }
}
