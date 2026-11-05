package com.example.mycycle

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import com.example.mycycle.achievements.AchievementsScreen
import com.example.mycycle.calendar.CalendarScreen
import com.example.mycycle.education.EducationScreen
import com.example.mycycle.home.HomeScreen
import com.example.mycycle.journal.JournalScreen
import com.example.mycycle.login.LoginScreen
import com.example.mycycle.notifications.NotificationHelper
import com.example.mycycle.notifications.NotificationScheduler
import com.example.mycycle.period.PeriodScreen
import com.example.mycycle.register.RegisterScreen
import com.example.mycycle.settings.SettingsScreen
import com.example.mycycle.ui.theme.MyCycleTheme

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted ->
            if (isGranted) {
                NotificationHelper.createNotificationChannel(this)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Create notification channel
        NotificationHelper.createNotificationChannel(this)

        // Schedule automatic daily tracking reminder
        NotificationScheduler.scheduleDailyReminder(this)

        // Request permission on Android 13 and above
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notificationPermissionLauncher.launch(
                Manifest.permission.POST_NOTIFICATIONS
            )
        }

        setContent {
            MyCycleTheme {

                var showRegister by remember {
                    mutableStateOf(false)
                }

                var loggedIn by remember {
                    mutableStateOf(false)
                }

                var showSettings by remember {
                    mutableStateOf(false)
                }

                var showPeriod by remember {
                    mutableStateOf(false)
                }

                var showCalendar by remember {
                    mutableStateOf(false)
                }

                var showJournal by remember {
                    mutableStateOf(false)
                }

                var showAchievements by remember {
                    mutableStateOf(false)
                }
                var showEducation by remember {
                    mutableStateOf(false)
                }

                var loggedInUserId by remember {
                    mutableStateOf(0)
                }

                when {

                    showEducation -> {
                        EducationScreen(
                            onBack = {
                                showEducation = false
                            }
                        )
                    }

                    showAchievements -> {
                        AchievementsScreen(
                            userId = loggedInUserId,
                            onBack = {
                                showAchievements = false
                            }
                        )
                    }

                    showJournal -> {
                        JournalScreen(
                            userId = loggedInUserId,
                            onBack = {
                                showJournal = false
                            }
                        )
                    }

                    showCalendar -> {
                        CalendarScreen(
                            userId = loggedInUserId,
                            onBack = {
                                showCalendar = false
                            }
                        )
                    }

                    showPeriod -> {
                        PeriodScreen(
                            userId = loggedInUserId,
                            onBack = {
                                showPeriod = false
                            }
                        )
                    }

                    showSettings -> {
                        SettingsScreen(
                            userId = loggedInUserId,
                            onBack = {
                                showSettings = false
                            },
                            onDeleteAccount = {
                                showSettings = false
                                loggedIn = false
                                loggedInUserId = 0
                            }
                        )
                    }

                    loggedIn -> {
                        HomeScreen(
                            onLogout = {
                                loggedIn = false
                                loggedInUserId = 0
                            },
                            onSettings = {
                                showSettings = true
                            },
                            onTrackPeriod = {
                                showPeriod = true
                            },
                            onCalendar = {
                                showCalendar = true
                            },
                            onJournal = {
                                showJournal = true
                            },
                            onAchievements = {
                                showAchievements = true
                            },
                            onEducation = {
                                showEducation = true
                            }
                        )
                    }

                    showRegister -> {
                        RegisterScreen(
                            onRegisterSuccess = {
                                showRegister = false
                            },
                            onLoginClick = {
                                showRegister = false
                            }
                        )
                    }

                    else -> {
                        LoginScreen(
                            onLoginSuccess = { userId ->
                                loggedInUserId = userId
                                loggedIn = true
                            },
                            onRegisterClick = {
                                showRegister = true
                            }
                        )
                    }
                }
            }
        }
    }
}
