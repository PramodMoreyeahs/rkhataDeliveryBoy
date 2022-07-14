package in.moreyeahs.livspace.delivery.utilities;

import android.app.Application;
import android.content.Context;

/**
 * Created by user on 5/26/2017.
 */
public class MyApplication extends Application {
    private static Context appContext;
    private static MyApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = this;
    }

    public static synchronized MyApplication getInstance() {
        return mInstance;
    }
}