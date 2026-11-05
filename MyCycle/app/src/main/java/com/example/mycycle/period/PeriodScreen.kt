package com.example.mycycle.period

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

import com.example.mycycle.AppLanguage
import com.example.mycycle.api.PeriodRequest
import com.example.mycycle.api.CosmosCycleRecordRequest
import com.example.mycycle.api.CosmosCycleRecordResponse
import com.example.mycycle.api.RetrofitClient
import com.example.mycycle.database.PeriodEntity
import com.example.mycycle.database.PeriodLocalStorage
import com.example.mycycle.database.PeriodSyncScheduler

import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun PeriodScreen(
    userId: Int,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    fun t(key: String): String {
        return AppLanguage.get(context, key)
    }

    var startDate by remember { mutableStateOf("") }
    var endDate by remember { mutableStateOf("") }
    var symptoms by remember { mutableStateOf("") }
    var mood by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // COSMOS DB RECORDS
    var cycleRecords by remember {
        mutableStateOf<List<CosmosCycleRecordResponse>>(emptyList())
    }

    var isLoadingRecords by remember {
        mutableStateOf(false)
    }

    // DATE VALIDATION
    fun validDate(value: String): Boolean {
        return try {
            val formatter = SimpleDateFormat(
                "yyyy-MM-dd",
                Locale.US
            )
            formatter.isLenient = false

            val date = formatter.parse(value)
            date != null && formatter.format(date) == value
        } catch (e: Exception) {
            false
        }
    }

    // LOAD RECORDS FROM COSMOS DB
    suspend fun loadCosmosRecords() {
        if (userId <= 0) return

        isLoadingRecords = true

        try {
            val response = RetrofitClient.api.getCycleRecords(
                userId.toString()
            )

            if (response.isSuccessful) {
                cycleRecords = response.body() ?: emptyList()
            } else {
                message = "Could not retrieve Cosmos DB records."
            }

        } catch (e: Exception) {
            message = "Unable to connect to Cosmos DB."
        } finally {
            isLoadingRecords = false
        }
    }

    // LOAD RECORDS WHEN SCREEN OPENS
    LaunchedEffect(userId) {
        loadCosmosRecords()
    }

    // RETRY PENDING OFFLINE PERIODS
    LaunchedEffect(userId) {
        if (userId <= 0) return@LaunchedEffect

        val pending = PeriodLocalStorage.getPendingPeriods(
            context,
            userId
        )

        for (period in pending) {
            try {
                val response = RetrofitClient.api.savePeriod(
                    PeriodRequest(
                        userId = userId,
                        startDate = period.startDate,
                        endDate = period.endDate
                    )
                )

                if (response.isSuccessful) {

                    var cosmosSaved = false

                    try {
                        val symptomList = period.symptoms
                            .split(",")
                            .map { it.trim() }
                            .filter { it.isNotEmpty() }

                        val cosmosResponse =
                            RetrofitClient.api.saveCycleRecord(
                                CosmosCycleRecordRequest(
                                    userId = userId.toString(),
                                    startDate = period.startDate,
                                    endDate = period.endDate,
                                    symptoms = symptomList,
                                    mood = period.mood,
                                    notes = period.notes
                                )
                            )

                        cosmosSaved = cosmosResponse.isSuccessful

                    } catch (e: Exception) {
                        cosmosSaved = false
                    }

                    // Only remove when both uploads succeed.
                    if (cosmosSaved) {
                        PeriodLocalStorage.removePendingPeriod(
                            context,
                            period
                        )
                    }
                }

            } catch (e: Exception) {
                // Keep pending record for another retry.
            }
        }

        loadCosmosRecords()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = t("Track My Period"),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(
            value = startDate,
            onValueChange = {
                startDate = it
                message = ""
            },
            label = {
                Text(t("Start Date (YYYY-MM-DD)"))
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = endDate,
            onValueChange = {
                endDate = it
                message = ""
            },
            label = {
                Text(t("End Date (YYYY-MM-DD)"))
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = symptoms,
            onValueChange = {
                symptoms = it
                message = ""
            },
            label = {
                Text(t("Symptoms (comma-separated)"))
            },
            placeholder = {
                Text(t("Cramps, headache, fatigue"))
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = mood,
            onValueChange = {
                mood = it
                message = ""
            },
            label = {
                Text(t("Mood"))
            },
            placeholder = {
                Text(t("Happy, sad, tired..."))
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = notes,
            onValueChange = {
                notes = it
                message = ""
            },
            label = {
                Text(t("Additional notes"))
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        // SAVE PERIOD BUTTON
        Button(
            onClick = {

                if (userId <= 0) {
                    message = t("User session is invalid. Please login again.")
                    return@Button
                }

                if (!validDate(startDate) || !validDate(endDate)) {
                    message = t("Enter valid dates using YYYY-MM-DD.")
                    return@Button
                }

                if (endDate < startDate) {
                    message = t("End date cannot be before start date.")
                    return@Button
                }

                isLoading = true
                message = ""

                scope.launch {

                    val period = PeriodEntity(
                        userId = userId,
                        startDate = "${startDate}T00:00:00",
                        endDate = "${endDate}T00:00:00",
                        symptoms = symptoms,
                        mood = mood,
                        notes = notes
                    )

                    try {
                        // SAVE LOCALLY FIRST
                        PeriodLocalStorage.saveLocalPeriod(
                            context,
                            period
                        )

                        PeriodLocalStorage.addPendingPeriod(
                            context,
                            period
                        )

                        PeriodSyncScheduler.schedule(
                            context,
                            userId
                        )

                        // SAVE TO REST API
                        val periodResponse =
                            RetrofitClient.api.savePeriod(
                                PeriodRequest(
                                    userId = userId,
                                    startDate = period.startDate,
                                    endDate = period.endDate
                                )
                            )

                        if (!periodResponse.isSuccessful) {
                            message = "Saved locally. Server upload is pending."
                        } else {

                            // SAVE TO COSMOS DB
                            val symptomList = symptoms
                                .split(",")
                                .map { it.trim() }
                                .filter { it.isNotEmpty() }

                            val cosmosResponse =
                                RetrofitClient.api.saveCycleRecord(
                                    CosmosCycleRecordRequest(
                                        userId = userId.toString(),
                                        startDate = period.startDate,
                                        endDate = period.endDate,
                                        symptoms = symptomList,
                                        mood = mood,
                                        notes = notes
                                    )
                                )

                            if (cosmosResponse.isSuccessful) {

                                PeriodLocalStorage.removePendingPeriod(
                                    context,
                                    period
                                )

                                message = "Period and Cosmos DB record saved successfully!"

                                // REFRESH RECORDS
                                loadCosmosRecords()

                            } else {
                                message = "Period saved, but Cosmos DB upload failed. It will be retried."
                            }
                        }

                        startDate = ""
                        endDate = ""
                        symptoms = ""
                        mood = ""
                        notes = ""

                    } catch (e: Exception) {

                        message = "Saved locally. Upload will be retried when connected."

                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(
                if (isLoading) "Saving..."
                else t("Save Period")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // BACK BUTTON
        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(t("Back to Home"))
        }

        Spacer(modifier = Modifier.height(24.dp))

        // COSMOS DB DATA DISPLAY
        Text(
            text = t("History (from Cosmos DB)"),
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        if (isLoadingRecords) {
            CircularProgressIndicator()
        } else if (cycleRecords.isEmpty()) {
            Text(t("No records found in Cosmos DB."))
        } else {
            cycleRecords.forEach { record ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = "${t("Period")}: ${record.startDate} -> ${record.endDate ?: "N/A"}",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Text(
                            text = "${t("Symptoms")}: ${record.symptoms.joinToString(", ")}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "${t("Mood")}: ${record.mood}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (record.notes.isNotEmpty()) {
                            Text(
                                text = "${t("Notes")}: ${record.notes}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }
        }
    }
}
