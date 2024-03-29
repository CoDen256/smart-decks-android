package coden.decks.android.app;

import android.app.Application;
import android.content.res.Resources;

import coden.decks.android.core.CoreApplicationComponent;
import coden.decks.android.core.DaggerCoreApplicationComponent;
import coden.decks.android.revision.notification.Notifications;
import coden.decks.android.revision.schedule.RevisionScheduler;

public class App extends Application {
    private static App mInstance;
    private static Resources res;
    public final static CoreApplicationComponent appComponent = DaggerCoreApplicationComponent.create();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        res = getResources();
        Notifications.createNotificationChannel(this);
        RevisionScheduler.scheduleRevision(this);
    }

    public static App getInstance() {
        return mInstance;
    }

    public static Resources getRes() {
        return res;
    }



}
