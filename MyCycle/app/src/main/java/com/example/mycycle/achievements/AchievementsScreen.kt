package com.example.mycycle.achievements

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
import androidx.compose.ui.unit.dp
import com.example.mycycle.api.RetrofitClient

@Composable
fun AchievementsScreen(
    userId: Int,
    onBack: () -> Unit
) {
    var periodCount by remember {
        mutableStateOf(0)
    }

    var journalCount by remember {
        mutableStateOf(0)
    }

    var message by remember {
        mutableStateOf("Loading achievements...")
    }

    var isLoading by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(userId) {

        if (userId <= 0) {
            message = "User session is invalid."
            isLoading = false
            return@LaunchedEffect
        }

        try {

            val periodResponse =
                RetrofitClient.api.getPeriods(userId)

            val journalResponse =
                RetrofitClient.api.getJournal(userId)

            if (periodResponse.isSuccessful) {
                periodCount =
                    periodResponse.body()?.size ?: 0
            }

            if (journalResponse.isSuccessful) {
                journalCount =
                    journalResponse.body()?.size ?: 0
            }

            message = ""

        } catch (e: Exception) {

            message =
                e.message
                    ?: "Unable to load achievements."

        } finally {

            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "My Achievements",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        if (isLoading) {

            Text(
                text = message
            )

        } else {

            Text(
                text = "🌸 First Entry",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text =
                    if (periodCount >= 1)
                        "✓ Unlocked - You recorded your first period."
                    else
                        "🔒 Record your first period to unlock."
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "🔥 7-Day Tracker",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text =
                    if (journalCount >= 7)
                        "✓ Unlocked - You tracked 7 entries."
                    else
                        "🔒 Track 7 journal entries to unlock."
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "🩷 Cycle Champion",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text =
                    if (periodCount >= 3)
                        "✓ Unlocked - You recorded 3 cycles."
                    else
                        "🔒 Record 3 periods to unlock."
            )

            Spacer(
                modifier = Modifier.height(16.dp)
            )

            Text(
                text = "⭐ Tracking Streak",
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text =
                    "You currently have $journalCount journal entries."
            )

            Spacer(
                modifier = Modifier.height(24.dp)
            )

            Text(
                text =
                    "Keep tracking your cycle and symptoms " +
                            "to unlock more achievements!"
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }
    }
}
