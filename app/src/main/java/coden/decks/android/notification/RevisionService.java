package coden.decks.android.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.job.JobParameters;
import android.app.job.JobService;

import java.util.List;

import javax.inject.Inject;

import coden.decks.android.R;
import coden.decks.android.app.App;
import coden.decks.core.data.Card;
import coden.decks.core.model.DecksModel;

public class RevisionService extends JobService {

    private static final int THRESHOLD = 5;

    @Inject
    DecksModel mDecksModel;

    public RevisionService() {
        App.appComponent.inject(this);
    }

    @Override
    public boolean onStartJob(JobParameters params) {
        mDecksModel.getReadyCards().thenAccept(this::checkAndNotify);
        return true;
    }

    private void checkAndNotify(List<Card> cards){
        if (!cards.isEmpty() && cards.size() > THRESHOLD)
            sendNotification(cards.size());
    }

    private void sendNotification(int wordsCount){
        NotificationManager notifyManager = getSystemService(NotificationManager.class);
        Notification notification = RevisionNotification.builder(this)
                .setContentTitle(getString(R.string.notification_title))
                .setContentText(String.format(getString(R.string.notification_text_template), wordsCount))
                .build();
        if (notifyManager == null) return;
        notifyManager.notify(0, notification);
    }







    private void reschedule(){
        RevisionScheduler.scheduleRevision(this);
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return true;
    }
}
