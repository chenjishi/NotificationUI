package android.os;
interface ITestService {
/**
* {@hide}
*/
	void setValue(int val);

	void registerNotification(ComponentName name, int user);
}