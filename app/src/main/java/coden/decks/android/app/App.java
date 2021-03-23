package coden.decks.android.app;

import android.app.Application;
import android.content.res.Resources;

import coden.decks.android.core.CoreApplicationComponent;
import coden.decks.android.core.DaggerCoreApplicationComponent;

public class App extends Application {
    private static App mInstance;
    private static Resources res;
    private static CoreApplicationComponent coreApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        res = getResources();
        coreApplicationComponent = DaggerCoreApplicationComponent.create(); // TODO: may be there is a better way?
    }

    public static App getInstance() {
        return mInstance;
    }

    public static Resources getRes() {
        return res;
    }

    public static CoreApplicationComponent getCoreApplicationComponent(){
        return coreApplicationComponent;
    }

}
