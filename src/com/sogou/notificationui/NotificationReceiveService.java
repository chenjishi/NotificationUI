package com.sogou.notificationui;

import android.app.Notification;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

/**
 * Created by chenjishi on 16/4/13.
 */
public class NotificationReceiveService extends NotificationListenerService {

    private NotificationData mNotificationData;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationData = NotificationData.getInstance();
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationPosted(sbn, rankingMap);

        if (null != sbn) {
            mNotificationData.add(sbn);
        }

        Notification notification = sbn.getNotification();
        if (null != notification) {
            Intent intent = new Intent(this, NotificationPanelService.class);
            intent.putExtra("notification", sbn);
            startService(intent);
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn, RankingMap rankingMap) {
        super.onNotificationRemoved(sbn, rankingMap);
        mNotificationData.remove(sbn.getKey());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mNotificationData.clear();
    }
}
