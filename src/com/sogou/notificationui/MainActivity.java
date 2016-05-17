package com.sogou.notificationui;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

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
}
