package com.example.mycycle.settings

import android.content.Context
import com.example.mycycle.NotificationHelper
import com.example.mycycle.AppLanguage
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
 import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.mycycle.api.RetrofitClient
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen(
    userId: Int,
    onBack: () -> Unit,
    onDeleteAccount: () -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val preferences = remember {
        context.getSharedPreferences(
            "MyCycleSettings",
            Context.MODE_PRIVATE
        )
    }

    var selectedLanguage by remember {
        mutableStateOf(
            preferences.getString(
                "language",
                "English"
            ) ?: "English"
        )
    }

    var upcomingPeriodReminder by remember {
        mutableStateOf(
            preferences.getBoolean(
                "upcomingPeriodReminder",
                true
            )
        )
    }

    var periodStartReminder by remember {
        mutableStateOf(
            preferences.getBoolean(
                "periodStartReminder",
                true
            )
        )
    }

    var cycleUpdateReminder by remember {
        mutableStateOf(
            preferences.getBoolean(
                "cycleUpdateReminder",
                true
            )
        )
    }

    var trackingReminder by remember {
        mutableStateOf(
            preferences.getBoolean(
                "trackingReminder",
                false
            )
        )
    }

    var message by remember {
        mutableStateOf("")
    }

    var isLoading by remember {
        mutableStateOf(false)
    }

    fun saveSettings() {
        preferences.edit()
            .putString(
                "language",
                selectedLanguage
            )
            .putBoolean(
                "upcomingPeriodReminder",
                upcomingPeriodReminder
            )
            .putBoolean(
                "periodStartReminder",
                periodStartReminder
            )
            .putBoolean(
                "cycleUpdateReminder",
                cycleUpdateReminder
            )
            .putBoolean(
                "trackingReminder",
                trackingReminder
            )
            .apply()

        message = "Settings saved successfully."
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(
                rememberScrollState()
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Language",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        LanguageOption(
            language = "English",
            selectedLanguage = selectedLanguage,
            onSelect = {
                selectedLanguage = "English"
            }
        )

        LanguageOption(
            language = "isiZulu",
            selectedLanguage = selectedLanguage,
            onSelect = {
                selectedLanguage = "isiZulu"
            }
        )

        LanguageOption(
            language = "Sepedi",
            selectedLanguage = selectedLanguage,
            onSelect = {
                selectedLanguage = "Sepedi"
            }
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Notifications",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        SettingSwitch(
            title = "Upcoming period reminder",
            checked = upcomingPeriodReminder,
            onCheckedChange = {
                upcomingPeriodReminder = it
            }
        )

        SettingSwitch(
            title = "Period start reminder",
            checked = periodStartReminder,
            onCheckedChange = {
                periodStartReminder = it
            }
        )

        SettingSwitch(
            title = "Cycle update reminder",
            checked = cycleUpdateReminder,
            onCheckedChange = {
                cycleUpdateReminder = it
            }
        )

        SettingSwitch(
            title = "Tracking reminder",
            checked = trackingReminder,
            onCheckedChange = {
                trackingReminder = it
            }
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Text(
            text = "Privacy & Security",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "MyCycle sends account information to the MyCycle API " +
                        "for authentication and data storage. Passwords are " +
                        "processed securely by the server. Do not share your " +
                        "account password with other people."
        )

        Spacer(
            modifier = Modifier.height(16.dp)
        )

        Text(
            text =
                "MyCycle provides cycle tracking and general " +
                        "educational information. It does not replace advice " +
                        "from a qualified healthcare professional."
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        OutlinedButton(
            onClick = {
                if (userId <= 0) {
                    message = "Invalid user session."
                    return@OutlinedButton
                }

                isLoading = true
                message = ""

                scope.launch {
                    try {
                        val response = RetrofitClient.api.deleteUser(userId)
                        if (response.isSuccessful) {
                            onDeleteAccount()
                        } else {
                            message = "Failed to delete account. Please try again."
                        }
                    } catch (e: Exception) {
                        message = e.message ?: "Unable to connect to the server."
                    } finally {
                        isLoading = false
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text(if (isLoading) "Deleting..." else "Delete Personal Data")
        }

        if (message.isNotEmpty()) {

            Spacer(
                modifier = Modifier.height(12.dp)
            )

            Text(
                text = message,
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = {
                saveSettings()

                if (trackingReminder) {
                    NotificationHelper.showTrackingReminder(context)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Save Settings")
        }

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth(),
            enabled = !isLoading
        ) {
            Text("Back to Home")
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}

@Composable
private fun LanguageOption(
    language: String,
    selectedLanguage: String,
    onSelect: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        RadioButton(
            selected = selectedLanguage == language,
            onClick = onSelect
        )

        Text(
            text = language
        )
    }
}

@Composable
private fun SettingSwitch(
    title: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = title,
            modifier = Modifier.weight(1f)
        )

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
    }

    Spacer(
        modifier = Modifier.height(8.dp)
    )
}
