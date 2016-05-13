package com.sogou.notificationui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private EntryListAdapter mListAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mListAdapter = new EntryListAdapter(this);
        ListView listView = (ListView) findViewById(R.id.list_view);
        listView.setAdapter(mListAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        mListAdapter.updateData();
    }

    public void onNotificationEnableButtonClicked(View view) {
//        startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
        Intent intent = new Intent(this, NotificationPanelService.class);
        startService(intent);
    }

    private void changeLabelStatus() {
        TextView textView = (TextView) findViewById(R.id.btn_open);
        textView.setTextColor(isNotificationEnabled() ? Color.WHITE : Color.RED);
    }

    private boolean isNotificationEnabled() {
        ContentResolver contentResolver = getContentResolver();
        String enabledListeners = Settings.Secure.getString(contentResolver, "enabled_notification_listeners");

        if (!TextUtils.isEmpty(enabledListeners)) {
            return enabledListeners.contains(getPackageName() + "/" + getPackageName() + ".NotificationReceiveService");
        } else {
            return false;
        }
    }

    private void showDialog() {
        if (!isNotificationEnabled()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.tip)
                    .setMessage(R.string.tip_message)
                    .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS"));
                        }
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        }
    }
}
