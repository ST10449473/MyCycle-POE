package com.example.mycycle.database

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters

import com.example.mycycle.api.PeriodRequest
import com.example.mycycle.api.RetrofitClient

class PeriodSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        val userId = inputData.getInt("userId", -1)

        if (userId <= 0) {
            return Result.failure()
        }

        val pendingPeriods =
            PeriodLocalStorage.getPendingPeriods(
                applicationContext,
                userId
            )

        if (pendingPeriods.isEmpty()) {
            return Result.success()
        }

        return try {

            for (period in pendingPeriods) {

                val response = RetrofitClient.api.savePeriod(
                    PeriodRequest(
                        userId = userId,
                        startDate = period.startDate,
                        endDate = period.endDate
                    )
                )

                if (!response.isSuccessful) {
                    return Result.retry()
                }

                // Remove the period after successful API upload.
                PeriodLocalStorage.removePendingPeriod(
                    applicationContext,
                    period
                )
            }

            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }
}

object PeriodSyncScheduler {

    fun schedule(context: Context, userId: Int) {

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request =
            OneTimeWorkRequestBuilder<PeriodSyncWorker>()
                .setConstraints(constraints)
                .setInputData(
                    androidx.work.workDataOf("userId" to userId)
                )
                .build()

        WorkManager.getInstance(context)
            .enqueueUniqueWork(
                "MyCyclePeriodSync_$userId",
                ExistingWorkPolicy.KEEP,
                request
            )
    }
}