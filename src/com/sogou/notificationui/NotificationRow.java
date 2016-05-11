package com.sogou.notificationui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.service.notification.StatusBarNotification;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RemoteViews;

/**
 * Created by chenjishi on 16/4/11.
 */
public class NotificationRow extends RelativeLayout {

    private StatusBarNotification mNotification;

    private LinearLayout mContainer;

    private ActionClickListener mListener;

    public NotificationRow(Context context) {
        super(context);
        init(context);
    }

    public NotificationRow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public NotificationRow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setBackgroundColor(Color.WHITE);
//        LayoutInflater.from(context).inflate(R.layout.notification_row, this, true);
        mContainer = new LinearLayout(context);
        mContainer.setOrientation(LinearLayout.VERTICAL);
        addView(mContainer, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }

    public void setActionClickListener(ActionClickListener listener) {
        mListener = listener;
    }

    public void setNotification(StatusBarNotification sbn) {
        if (null == sbn) return;

        mNotification = sbn;

        RemoteViews remoteViews = sbn.getNotification().contentView;

        if (null != remoteViews) {
            View localView = remoteViews.apply(getContext(), mContainer, mRemoteClickHander);
            localView.setIsRootNamespace(true);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mContainer.addView(localView, lp);
        }

        RemoteViews bigContentView = sbn.getNotification().bigContentView;
        if (null != bigContentView) {
            View bigLocalView = bigContentView.apply(getContext(), mContainer, mRemoteClickHander);
            bigLocalView.setIsRootNamespace(true);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            mContainer.addView(bigLocalView, lp);
        }
    }

    public interface ActionClickListener {

        void onActionClicked(PendingIntent pendingIntent);

    }

    private final RemoteViews.OnClickHandler mRemoteClickHander = new RemoteViews.OnClickHandler() {
        @Override
        public boolean onClickHandler(View view, PendingIntent pendingIntent, Intent fillInIntent) {
            if (null != mListener) {
                mListener.onActionClicked(pendingIntent);
            }

            return true;
        }
    };
}
