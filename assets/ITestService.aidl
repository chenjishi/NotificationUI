package android.os;
interface ITestService {
/**
* {@hide}
*/
	void setValue(int val);

	void registerNotification(Context context, ComponentName name, int user);
}