package com.example.mycycle.education

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun EducationScreen(
    onBack: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {

        Text(
            text = "Menstrual Health Education",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "What is a menstrual cycle?",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "A menstrual cycle is the monthly process in which " +
                        "the body prepares for a possible pregnancy. The cycle " +
                        "includes changes in hormones, the ovaries and the " +
                        "lining of the uterus."
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "The Four Cycle Phases",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "1. Menstruation",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text =
                "This is when menstrual bleeding occurs. The uterine " +
                        "lining is shed when pregnancy has not occurred."
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "2. Follicular Phase",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text =
                "During this phase, follicles in the ovaries develop " +
                        "and the uterine lining begins to build up again."
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "3. Ovulation",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text =
                "Ovulation is when an ovary releases an egg. The timing " +
                        "of ovulation can vary between people and between cycles."
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        Text(
            text = "4. Luteal Phase",
            style = MaterialTheme.typography.titleMedium
        )

        Text(
            text =
                "After ovulation, hormone levels change as the body " +
                        "prepares for a possible pregnancy."
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Common Period Symptoms",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "Some people experience symptoms such as cramps, " +
                        "headaches, bloating, fatigue, back pain and breast " +
                        "tenderness. Symptoms can vary from person to person."
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Tracking Your Cycle",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "Recording your period dates, symptoms and mood can " +
                        "help you understand patterns in your cycle over time. " +
                        "MyCycle uses previous entries to provide estimated " +
                        "cycle information."
        )

        Spacer(
            modifier = Modifier.height(20.dp)
        )

        Text(
            text = "Important Information",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.height(8.dp)
        )

        Text(
            text =
                "MyCycle provides general educational information and " +
                        "cycle estimates. Predictions are not medically " +
                        "accurate and should not be used as contraception. " +
                        "MyCycle does not replace advice from a qualified " +
                        "healthcare professional."
        )

        Spacer(
            modifier = Modifier.height(24.dp)
        )

        Button(
            onClick = onBack,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Back to Home")
        }

        Spacer(
            modifier = Modifier.height(20.dp)
        )
    }
}

