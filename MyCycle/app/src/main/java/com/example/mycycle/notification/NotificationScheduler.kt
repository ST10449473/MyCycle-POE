package com.example.mycycle.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.OneTimeWorkRequestBuilder
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    private const val UPCOMING_PERIOD_WORK_NAME = "UpcomingPeriodReminder"

    fun scheduleDailyReminder(context: Context) {

        val reminder = PeriodicWorkRequestBuilder<PeriodReminderWorker>(
            24, TimeUnit.HOURS
        ).build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "MyCycleDailyReminder",
            ExistingPeriodicWorkPolicy.UPDATE,
            reminder
        )
    }

    fun scheduleUpcomingPeriodReminder(context: Context, timeInMillis: Long) {
        // Schedule reminder 1 day before the predicted period
        val reminderTime = timeInMillis - TimeUnit.DAYS.toMillis(1)
        val delay = reminderTime - System.currentTimeMillis()

        if (delay > 0) {
            val reminder = OneTimeWorkRequestBuilder<PeriodReminderWorker>()
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                UPCOMING_PERIOD_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                reminder
            )
        }
    }

    fun cancelUpcomingPeriodReminder(context: Context) {
        WorkManager.getInstance(context).cancelUniqueWork(UPCOMING_PERIOD_WORK_NAME)
    }

    fun scheduleTestReminder(context: Context) {

        val testReminder =
            OneTimeWorkRequestBuilder<PeriodReminderWorker>()
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build()

        WorkManager.getInstance(context).enqueue(testReminder)
    }
}