package com.sogou.notificationui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by chenjishi on 16/5/17.
 */
public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.i("test", "#Boot Complete");
        context.startService(new Intent(context, NotificationPanelService.class));
    }
}
