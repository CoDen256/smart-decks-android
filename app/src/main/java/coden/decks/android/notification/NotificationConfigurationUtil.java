package coden.decks.android.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;

import coden.decks.android.R;

public class NotificationConfigurationUtil {

    public static final String CHANNEL_ID = "SMART-DECKS-NOTIFICATION-CHANNEL";
    private final Context mContext;

    public NotificationConfigurationUtil(Context context) {
        mContext = context;
    }

    public void createNotificationChannel() {
        CharSequence name = mContext.getString(R.string.channel_name);
        String description = mContext.getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
        channel.setDescription(description);

        NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
        if (notificationManager == null) throw  new IllegalStateException("Unable to create NotificationManager");
        notificationManager.createNotificationChannel(channel);
    }
}
