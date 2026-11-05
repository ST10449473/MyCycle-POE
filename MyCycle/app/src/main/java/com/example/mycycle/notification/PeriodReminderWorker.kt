
package com.example.mycycle.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class PeriodReminderWorker(
    context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {

        NotificationHelper.createNotificationChannel(
            applicationContext
        )

        val title = inputData.getString("title")
            ?: "MyCycle Reminder"

        val message = inputData.getString("message")
            ?: "Remember to update your cycle tracking today."

        NotificationHelper.showNotification(
            applicationContext,
            title,
            message
        )

        return Result.success()
    }
}
