package com.sogou.notificationui;

import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.service.notification.StatusBarNotification;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by chenjishi on 16/4/25.
 */
public class EntryListAdapter extends BaseAdapter {

    private final ArrayList<StatusBarNotification> mDataList = new ArrayList<>();

    private LayoutInflater mInflater;

    private Context mContext;

    public EntryListAdapter(Context context) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
    }

    public void updateData() {
        ArrayList<StatusBarNotification> list = NotificationData.getInstance().getSortedList();
        if (null != list && list.size() > 0) {
            mDataList.clear();
            mDataList.addAll(list);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getCount() {
        return mDataList.size();
    }

    @Override
    public StatusBarNotification getItem(int position) {
        return mDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.cell_entry_layout, parent, false);

            holder.imageView = (ImageView) convertView.findViewById(R.id.image_view);
            holder.titleView = (TextView) convertView.findViewById(R.id.title_label);
            holder.textView = (TextView) convertView.findViewById(R.id.text_label);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StatusBarNotification sbn = getItem(position);

        if (null != sbn.getNotification().extras) {
            Bundle extras = sbn.getNotification().extras;
            String title = extras.getString(Notification.EXTRA_TITLE);
            holder.titleView.setText(title);

            String text = extras.getString(Notification.EXTRA_TEXT);
            holder.textView.setText(text);
        } else {
            holder.titleView.setText("");
            holder.textView.setText("");
        }

        String pkgName = sbn.getPackageName();
        try {
            Context remoteContext = mContext.createPackageContext(pkgName, 0);
            Drawable icon = remoteContext.getResources().getDrawable(sbn.getNotification().icon);
            holder.imageView.setImageDrawable(icon);
        } catch (PackageManager.NameNotFoundException e) {
        }


        return convertView;
    }

    private final class ViewHolder {

        public ImageView imageView;

        public TextView titleView;

        public TextView textView;

    }
}
