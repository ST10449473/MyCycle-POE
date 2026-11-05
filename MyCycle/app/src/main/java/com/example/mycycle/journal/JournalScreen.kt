package com.example.mycycle.journal

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mycycle.api.JournalRequest
import com.example.mycycle.api.RetrofitClient
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun JournalScreen(
    userId: Int,
    onBack: () -> Unit
) {
    var cramps by remember {
        mutableStateOf(false)
    }

    var headaches by remember {
        mutableStateOf(false)
    }

    var bloating by remember {
        mutableStateOf(false)
    }

    var fatigue by remember {
        mutableStateOf(false)
    }

    var backPain by remember {
        mutableStateOf(false)
    }

    var breastTenderness by remember {
        mutableStateOf(false)
    }

    var mood by remember {
        mutableStateOf("")
    }

    var message by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Symptoms & Mood",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text("Select your symptoms:")

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = if (cramps) "Selected" else "",
            onValueChange = {
                cramps = !cramps
            },
            label = {
                Text("Cramps")
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = if (headaches) "Selected" else "",
            onValueChange = {
                headaches = !headaches
            },
            label = {
                Text("Headaches")
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = if (bloating) "Selected" else "",
            onValueChange = {
                bloating = !bloating
            },
            label = {
                Text("Bloating")
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = if (fatigue) "Selected" else "",
            onValueChange = {
                fatigue = !fatigue
            },
            label = {
                Text("Fatigue")
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = if (backPain) "Selected" else "",
            onValueChange = {
                backPain = !backPain
            },
            label = {
                Text("Back Pain")
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        OutlinedTextField(
            value = if (breastTenderness) "Selected" else "",
            onValueChange = {
                breastTenderness = !breastTenderness
            },
            label = {
                Text("Breast Tenderness")
            },
            modifier = Modifier.fillMaxWidth(),
            readOnly = true
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        OutlinedTextField(
            value = mood,
            onValueChange = {
                mood = it
                message = ""
            },
            label = {
                Text("Mood")
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        if (message.isNotEmpty()) {

            Text(
                text = message,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )
        }

        Button(
            onClick = {

                if (userId <= 0) {
                    message = "User session is invalid. Please login again."
                    return@Button
                }

                val symptoms = buildList {

                    if (cramps) {
                        add("Cramps")
                    }

                    if (headaches) {
                        add("Headaches")
                    }

                    if (bloating) {
                        add("Bloating")
                    }

                    if (fatigue) {
                        add("Fatigue")
                    }

                    if (backPain) {
                        add("Back Pain")
                    }

                    if (breastTenderness) {
                        add("Breast Tenderness")
                    }

                }.joinToString(", ")

                if (symptoms.isBlank() && mood.isBlank()) {

                    message =
                        "Please select a symptom or enter your mood."

                    return@Button
                }

                isLoading = true
                message = ""

                scope.launch {

                    try {

                        val currentDate =
                            SimpleDateFormat(
                                "yyyy-MM-dd'T'00:00:00",
                                Locale.getDefault()
                            ).format(Date())

                        val response =
                            RetrofitClient.api.saveJournal(
                                JournalRequest(
                                    userId = userId,
                                    symptoms = symptoms,
                                    mood = mood,
                                    entryDate = currentDate
                                )
                            )

                        if (response.isSuccessful) {

                            message =
                                "Symptoms and mood saved successfully."

                        } else {

                            message =
                                response.errorBody()?.string()
                                    ?: "Unable to save journal entry."
                        }

                    } catch (e: Exception) {

                        message =
                            e.message
                                ?: "Unable to connect to the server."

                    } finally {

                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {

            Text(
                if (isLoading) {
                    "Saving..."
                } else {
                    "Save Entry"
                }
            )
        }

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        TextButton(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {

            Text("Back")
        }
    }
}
