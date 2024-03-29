package coden.decks.android.revision.schedule;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import org.jetbrains.annotations.Nullable;

import coden.decks.android.revision.RevisionService;

public class RevisionScheduler extends BroadcastReceiver {

    private static final int JOB_ID = 2;
    private static final int INTERVAL_MILLIS = 60 * 1000 * 15;

    public static void scheduleRevision(Context context){
        JobScheduler scheduler = getJobScheduler(context);
        if (scheduler == null) return;

        ComponentName serviceName = new ComponentName(context, RevisionService.class);
        JobInfo info = new JobInfo.Builder(JOB_ID, serviceName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setOverrideDeadline(INTERVAL_MILLIS)
//                .setPeriodic(INTERVAL_MILLIS)
                .build();
        scheduler.schedule(info);
    }

    @Nullable
    private static JobScheduler getJobScheduler(Context context) {
        JobScheduler scheduler = context.getSystemService(JobScheduler.class);
        if(scheduler == null) {
            Toast.makeText(context, "Revision Scheduler is not created!", Toast.LENGTH_LONG).show();
            return null;
        }
        return scheduler;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        scheduleRevision(context);
    }
}
