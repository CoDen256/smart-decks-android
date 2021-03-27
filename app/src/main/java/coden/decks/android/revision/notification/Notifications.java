package coden.decks.android.revision.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import org.jetbrains.annotations.NotNull;

import coden.decks.android.R;
import coden.decks.android.SettingsActivity;

public class Notifications {

    public static final String CHANNEL_ID = "SMART-DECKS-NOTIFICATION-CHANNEL";

    public static void createNotificationChannel(Context context) {
        CharSequence name = context.getString(R.string.channel_name);
        String description = context.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = getNotificationManager(context);
        notificationManager.createNotificationChannel(channel);
    }

    public static NotificationCompat.Builder builder(Context context, Class<?> targetActivity){
        return new NotificationCompat.Builder(context, Notifications.CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_menu_week)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(createIntent(context, targetActivity))
                .setAutoCancel(true);
    }

    private static PendingIntent createIntent(Context context, Class<?> targetActivity){
        Intent intent = new Intent(context, targetActivity);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    public static void notify(Context context, Notification notification){
        getNotificationManager(context).notify(0, notification);
    }

    @NotNull
    private static NotificationManager getNotificationManager(Context context) {
        NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
        if (notificationManager == null) throw new IllegalStateException("Unable to create NotificationManager");
        return notificationManager;
    }
}
