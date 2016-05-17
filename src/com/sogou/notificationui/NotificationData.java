package com.sogou.notificationui;

import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;

import java.util.ArrayList;

/**
 * Created by chenjishi on 16/4/21.
 */
public class NotificationData {

    private static final NotificationData INSTANCE = new NotificationData();

    private final ArrayMap<String, StatusBarNotification> mEntries = new ArrayMap<>();

    private final ArrayList<StatusBarNotification> mSortedAndFiltered = new ArrayList<>();

    private NotificationData() {

    }

    public static NotificationData getInstance() {
        return INSTANCE;
    }

    public void add(StatusBarNotification sbn) {
        mEntries.put(sbn.getKey(), sbn);
    }

    public void remove(String key) {
        mEntries.remove(key);
    }

    public ArrayList<StatusBarNotification> getSortedList() {
        mSortedAndFiltered.clear();

        int n = mEntries.size();
        for (int i = 0; i < n; i++) {
            mSortedAndFiltered.add(mEntries.valueAt(i));
        }

        return mSortedAndFiltered;
    }

    public void clear() {
        mEntries.clear();
        mSortedAndFiltered.clear();
    }
}
