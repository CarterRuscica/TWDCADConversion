package com.example.twdcadconversion;

import android.app.Application;
import android.content.Context;

/**
 * Created by Jerry on 3/21/2018.
 */

public class GlobalApplication extends Application {
    /**
     * This method was because context was not available throughout
     */

    private static Context appContext;

    @Override
    public void onCreate() {
        super.onCreate();
        appContext = getApplicationContext();

        /* If you has other classes that need context object to initialize when application is created,
         you can use the appContext here to process. */
    }

    public static Context getAppContext() {
        return appContext;
    }
}