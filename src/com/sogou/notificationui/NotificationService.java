package com.sogou.notificationui;

import android.app.Notification;
import android.content.Intent;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

/**
 * Created by chenjishi on 16/3/31.
 */
public class NotificationService extends NotificationListenerService {

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        Notification notification = sbn.getNotification();

        if (null != notification) {
            Intent intent = new Intent(this, NotificationPanelService.class);
            intent.putExtra("notification", sbn);
            startService(intent);
        }

        Log.i("test", "##nofication received");
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        super.onNotificationRemoved(sbn);
    }
}
