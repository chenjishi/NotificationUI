package android.os;

import android.content.ComponentName;

interface ITestService {
/**
* {@hide}
*/
	void setValue(int val);

	void registerNotification(out ComponentName name, int user);
}