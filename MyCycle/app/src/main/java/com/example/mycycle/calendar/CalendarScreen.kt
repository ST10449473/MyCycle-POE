package com.example.mycycle.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import com.example.mycycle.AppLanguage
import com.example.mycycle.api.RetrofitClient
import com.example.mycycle.api.CosmosCycleRecordResponse
import com.example.mycycle.notifications.NotificationScheduler
import com.example.mycycle.database.PeriodEntity
import com.example.mycycle.database.PeriodLocalStorage

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@Composable
fun CalendarScreen(
    userId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    var periods by remember {
        mutableStateOf<List<CalendarPeriod>>(emptyList())
    }

    var cosmosRecords by remember {
        mutableStateOf<List<CosmosCycleRecordResponse>>(emptyList())
    }

    var message by remember {
        mutableStateOf("Loading your cycle history...")
    }

    var cosmosMessage by remember {
        mutableStateOf("")
    }

    var prediction by remember {
        mutableStateOf("")
    }

    var statistics by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    // Translate text using the selected language.
    fun t(key: String): String {
        return AppLanguage.get(context, key)
    }

    // Format dates for display.
    fun formatDate(date: Calendar): String {
        val formatter = SimpleDateFormat(
            "yyyy-MM-dd",
            Locale.getDefault()
        )

        return formatter.format(date.time)
    }

    // Calculate cycle and period statistics.
    fun calculateStatistics(
        inputPeriods: List<CalendarPeriod> = periods
    ) {
        if (inputPeriods.isEmpty()) {
            statistics = ""
            return
        }

        try {
            val formatter = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            )

            val sortedPeriods = inputPeriods.sortedBy {
                it.startDate.take(10)
            }

            val periodLengths = sortedPeriods.mapNotNull { period ->
                val start = formatter.parse(period.startDate.take(10))
                val end = formatter.parse(period.endDate.take(10))

                if (start != null && end != null) {
                    val difference = end.time - start.time

                    val days = (
                            difference / (1000L * 60 * 60 * 24)
                            ).toInt() + 1

                    if (days > 0) days else null
                } else {
                    null
                }
            }

            val averagePeriodLength =
                if (periodLengths.isNotEmpty()) {
                    periodLengths.average()
                } else {
                    0.0
                }

            val cycleLengths = mutableListOf<Int>()

            for (i in 1 until sortedPeriods.size) {
                val previous = formatter.parse(
                    sortedPeriods[i - 1].startDate.take(10)
                )

                val current = formatter.parse(
                    sortedPeriods[i].startDate.take(10)
                )

                if (previous != null && current != null) {
                    val difference = current.time - previous.time

                    val days = (
                            difference / (1000L * 60 * 60 * 24)
                            ).toInt()

                    if (days > 0) {
                        cycleLengths.add(days)
                    }
                }
            }

            val averageCycleLength =
                if (cycleLengths.isNotEmpty()) {
                    cycleLengths.average()
                } else {
                    0.0
                }

            statistics = buildString {
                append("${t("Periods recorded")}: ${inputPeriods.size}\n")

                append("${t("Average period length")}: ")

                if (averagePeriodLength > 0) {
                    append(
                        String.format(
                            Locale.getDefault(),
                            "%.1f %s",
                            averagePeriodLength,
                            t("days")
                        )
                    )
                } else {
                    append(t("Not enough data"))
                }

                append("\n${t("Average cycle length")}: ")

                if (averageCycleLength > 0) {
                    append(
                        String.format(
                            Locale.getDefault(),
                            "%.1f %s",
                            averageCycleLength,
                            t("days")
                        )
                    )
                } else {
                    append(t("Not enough data"))
                }
            }

        } catch (e: Exception) {
            statistics = t("Unable to calculate statistics.")
        }
    }

    // Calculate the next predicted period.
    fun calculatePrediction(
        inputPeriods: List<CalendarPeriod> = periods
    ) {
        if (inputPeriods.size < 2) {
            prediction = t(
                "Add another period entry to calculate a prediction."
            )

            NotificationScheduler.cancelUpcomingPeriodReminder(context)
            return
        }

        try {
            val formatter = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.getDefault()
            )

            val sortedPeriods = inputPeriods.sortedBy {
                it.startDate.take(10)
            }

            val previousDate = formatter.parse(
                sortedPeriods[sortedPeriods.size - 2]
                    .startDate.take(10)
            )

            val latestDate = formatter.parse(
                sortedPeriods.last().startDate.take(10)
            )

            if (previousDate == null || latestDate == null) {
                prediction = t("Unable to calculate prediction.")
                NotificationScheduler.cancelUpcomingPeriodReminder(context)
                return
            }

            val difference = latestDate.time - previousDate.time

            val cycleLength = (
                    difference / (1000L * 60 * 60 * 24)
                    ).toInt()

            if (cycleLength <= 0) {
                prediction = t("Unable to calculate prediction.")
                NotificationScheduler.cancelUpcomingPeriodReminder(context)
                return
            }

            val nextPeriod = Calendar.getInstance()
            nextPeriod.time = latestDate

            nextPeriod.add(
                Calendar.DAY_OF_YEAR,
                cycleLength
            )

            NotificationScheduler.scheduleUpcomingPeriodReminder(
                context,
                nextPeriod.timeInMillis
            )

            prediction =
                "${t("Estimated next period")}: ${formatDate(nextPeriod)}"

        } catch (e: Exception) {
            prediction = t("Unable to calculate prediction.")
            NotificationScheduler.cancelUpcomingPeriodReminder(context)
        }
    }

    // Load saved periods from local storage.
    fun loadOfflinePeriods() {
        val cachedPeriods = PeriodLocalStorage.getPeriods(
            context,
            userId
        )

        val loadedPeriods = cachedPeriods.map {
            CalendarPeriod(
                startDate = it.startDate,
                endDate = it.endDate
            )
        }

        periods = loadedPeriods

        if (loadedPeriods.isNotEmpty()) {
            message = t("Showing saved periods offline.")

            calculatePrediction(loadedPeriods)
            calculateStatistics(loadedPeriods)

        } else {
            message = t("No saved periods available offline.")

            prediction = ""
            statistics = ""

            NotificationScheduler.cancelUpcomingPeriodReminder(context)
        }
    }

    // Load period history and Cosmos DB records.
    LaunchedEffect(userId) {
        if (userId <= 0) {
            message = t("User session is invalid.")
            isLoading = false
            return@LaunchedEffect
        }

        // Load period history from the REST API.
        try {
            val response = RetrofitClient.api.getPeriods(userId)

            if (response.isSuccessful) {
                val data = response.body() ?: emptyList()

                val loadedPeriods = data.map {
                    CalendarPeriod(
                        startDate = it.startDate,
                        endDate = it.endDate
                    )
                }

                periods = loadedPeriods

                // Save API records locally for offline access.
                PeriodLocalStorage.savePeriods(
                    context,
                    userId,
                    loadedPeriods.map {
                        PeriodEntity(
                            userId = userId,
                            startDate = it.startDate,
                            endDate = it.endDate
                        )
                    }
                )

                if (loadedPeriods.isEmpty()) {
                    message = t("No period entries found.")

                    prediction = ""
                    statistics = ""

                    NotificationScheduler.cancelUpcomingPeriodReminder(
                        context
                    )

                } else {
                    message = ""

                    calculatePrediction(loadedPeriods)
                    calculateStatistics(loadedPeriods)
                }

            } else {
                loadOfflinePeriods()
            }

        } catch (e: Exception) {
            // If the API is unavailable, use locally saved periods.
            loadOfflinePeriods()
        }

        // Load NoSQL records from Azure Cosmos DB.
        try {
            val cosmosResponse =
                RetrofitClient.api.getCycleRecords(userId.toString())

            if (cosmosResponse.isSuccessful) {
                cosmosRecords =
                    cosmosResponse.body() ?: emptyList()

                cosmosMessage =
                    if (cosmosRecords.isEmpty()) {
                        t("No Cosmos DB records found yet.")
                    } else {
                        ""
                    }

            } else {
                cosmosMessage = t("Unable to load Cosmos DB records.")
            }

        } catch (e: Exception) {
            cosmosMessage = t("Cosmos DB records are unavailable offline.")
        }

        isLoading = false
    }

    // Screen layout.
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = t("Cycle Calendar"),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        if (isLoading) {
            Text(text = t("Loading your cycle history..."))

        } else {

            if (periods.isNotEmpty()) {

                Text(
                    text = t("Your saved periods:"),
                    style = MaterialTheme.typography.titleMedium
                )

                Spacer(modifier = Modifier.height(12.dp))

                periods.forEachIndexed { index, period ->
                    Text(
                        text = "${index + 1}. ${period.startDate.take(10)} " +
                                "${t("to")} ${period.endDate.take(10)}"
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (prediction.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = prediction,
                        style = MaterialTheme.typography.titleMedium
                    )
                }

                if (statistics.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = t("Cycle Statistics"),
                        style = MaterialTheme.typography.titleMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(text = statistics)
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = t(
                        "Predictions are estimates based on previous cycle data. " +
                                "They are not medically accurate and should not be used as contraception."
                    )
                )

            } else {
                Text(text = message)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = t("Azure Cosmos DB Records"),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (cosmosRecords.isEmpty()) {
                Text(text = cosmosMessage)

            } else {
                cosmosRecords.forEachIndexed { index, record ->

                    Text(
                        text = "${t("Record")} ${index + 1}",
                        style = MaterialTheme.typography.titleSmall
                    )

                    Text(
                        text = "${t("Start date")}: ${record.startDate.take(10)}"
                    )

                    Text(
                        text = "${t("End date")}: ${
                            record.endDate?.take(10) ?: t("Not recorded")
                        }"
                    )

                    Text(
                        text = "${t("Symptoms")}: ${
                            record.symptoms.joinToString(", ")
                        }"
                    )

                    Text(
                        text = "${t("Mood")}: ${record.mood}"
                    )

                    Text(
                        text = "${t("Notes")}: ${record.notes}"
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(t("Back to Home"))
        }
    }
}

data class CalendarPeriod(
    val startDate: String,
    val endDate: String
)
