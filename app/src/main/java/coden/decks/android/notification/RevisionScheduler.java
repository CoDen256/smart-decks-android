package coden.decks.android.notification;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

public class RevisionScheduler {

    private static final int JOB_ID = 2;
    private static final int INTERVAL_MILLIS = 60 * 1000 * 15;

    public static void scheduleRevision(Context context){
        JobScheduler scheduler = context.getSystemService(JobScheduler.class);

        ComponentName serviceName = new ComponentName(context, RevisionService.class);
        JobInfo info = new JobInfo.Builder(JOB_ID, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setOverrideDeadline(INTERVAL_MILLIS)
//                .setPeriodic(INTERVAL_MILLIS)
                .build();
        scheduler.schedule(info);
    }
}
