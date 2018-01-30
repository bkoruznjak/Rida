package hr.from.bkoruznjak.rida.jobs;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import hr.from.bkoruznjak.rida.root.AppScope;
import hr.from.bkoruznjak.rida.root.RidaApp;

/**
 * Created by bkoruznjak on 30/01/2018.
 */

@Module
public class JobModule {

    @AppScope
    @Provides
    JobScheduler provideJobScheduler(RidaApp context) {
        return (JobScheduler) context.getSystemService(Context.JOB_SCHEDULER_SERVICE);
    }

    @AppScope
    @Provides
    @Named("uploadJob")
    JobInfo provideNonUploadedItemsJob(RidaApp context) {
        return new JobInfo.Builder(NonUploadedJob.BACKGROUND_AUTH_JOB_ID,
                new ComponentName(context, NonUploadedJob.class))
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(NonUploadedJob.JOB_PERIOD_IN_MILLIS)
                .setPersisted(true)
                .build();
    }
}