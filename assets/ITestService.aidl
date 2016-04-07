package android.os;

import android.content.ComponentName;

interface ITestService {
/**
* {@hide}
*/
	void setValue(int val);

	void registerNotification(ComponentName name, int user);
}