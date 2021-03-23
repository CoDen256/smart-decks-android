package coden.decks.android.notification;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import coden.decks.android.SettingsActivity;

public class RevisionNotification {

    public static NotificationCompat.Builder builder(Context context){
        return new NotificationCompat.Builder(context, NotificationConfigurationUtil.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_week)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(createIntent(context))
                .setAutoCancel(true);
    }

    public static PendingIntent createIntent(Context context){
        Intent intent = new Intent(context, SettingsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

}
