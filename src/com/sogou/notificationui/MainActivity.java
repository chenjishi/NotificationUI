package com.sogou.notificationui;

import android.app.Activity;
import android.app.Notification;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.Log;
import android.widget.TextView;
import android.app.INotificationManager;

public class MainActivity extends Activity {

    private TextView mTextView;

    private INotificationManager mNoMan;
    private INotificationListenerWrapper mWrapper = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTextView = (TextView) findViewById(R.id.text_view);

        INotificationManager noMan = getNotificationInterface();
        if (mWrapper == null) {
            mWrapper = new INotificationListenerWrapper();
        }
        noMan.registerListener(mWrapper, new ComponentName(getPackageName(),
                getClass().getCanonicalName()), UserHandle.USER_ALL);


    }

    private final INotificationManager getNotificationInterface() {
        if (mNoMan == null) {
            mNoMan = INotificationManager.Stub.asInterface(
                    ServiceManager.getService(Context.NOTIFICATION_SERVICE));
        }
        return mNoMan;
    }

    private class INotificationListenerWrapper extends INotificationListener.Stub {
        @Override
        public void onNotificationPosted(IStatusBarNotificationHolder sbnHolder,
                                         NotificationRankingUpdate update) {

            Log.i("test", "###onNotificationPosted");
        }

        @Override
        public void onNotificationRemoved(IStatusBarNotificationHolder sbnHolder,
                                          NotificationRankingUpdate update) {

        }

        @Override
        public void onListenerConnected(NotificationRankingUpdate update) {
            Log.i("test", "onListenerConnected");

        }

        @Override
        public void onNotificationRankingUpdate(NotificationRankingUpdate update)
                throws RemoteException {

        }

        @Override
        public void onListenerHintsChanged(int hints) throws RemoteException {

        }

        @Override
        public void onInterruptionFilterChanged(int interruptionFilter) throws RemoteException {

        }
    }
}
