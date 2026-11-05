package com.example.mycycle.home

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mycycle.AppLanguage

@Composable
fun HomeScreen(
    onLogout: () -> Unit,
    onSettings: () -> Unit,
    onTrackPeriod: () -> Unit,
    onCalendar: () -> Unit,
    onJournal: () -> Unit,
    onAchievements: () -> Unit,
    onEducation: () -> Unit
) {
    val context = LocalContext.current

    fun t(key: String): String {
        return AppLanguage.get(context, key)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = t("Welcome to MyCycle"),
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = onTrackPeriod,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(t("Track My Period"))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onCalendar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(t("Cycle Calendar"))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onJournal,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(t("Symptoms & Mood"))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onAchievements,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(t("Achievements"))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onEducation,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(t("Health Education"))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onSettings,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(t("Settings"))
        }

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(t("Logout"))
        }
    }
}