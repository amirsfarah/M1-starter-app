package com.cpen321.usermanagement.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.cpen321.usermanagement.ui.viewmodels.ProfileViewModel
import com.cpen321.usermanagement.ui.viewmodels.TriviaCardUiState
import com.cpen321.usermanagement.ui.viewmodels.TriviaCardViewModel

@Composable
fun TriviaCard(
    triviaCardViewModel: TriviaCardViewModel,
    profileViewModel: ProfileViewModel,
    modifier: Modifier = Modifier
) {
    val ui = triviaCardViewModel.ui.collectAsState().value
    val profileUi = profileViewModel.uiState.collectAsState()
    val hobbies = profileUi.value.selectedHobbies.toList()

    LaunchedEffect(Unit) {
        triviaCardViewModel.loadNewQuestion(hobbies)
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            Text("Quick Trivia", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))

            when {
                ui.isLoading -> {
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                        CircularProgressIndicator()
                    }
                }
                ui.error != null -> {
                    Text(ui.error!!)
                    Spacer(Modifier.height(8.dp))
                    Button(onClick = { triviaCardViewModel.loadNewQuestion(hobbies) }) { Text("Try again") }
                }
                else -> {
                    if (ui.category.isNotEmpty()) {
                        Text("${ui.category} • ${ui.difficulty}", style = MaterialTheme.typography.labelMedium)
                        Spacer(Modifier.height(6.dp))
                    }
                    Text(ui.question, style = MaterialTheme.typography.titleSmall)
                    Spacer(Modifier.height(12.dp))

                    ui.options.forEachIndexed { idx, option ->
                        val isCorrect = idx == ui.correctIndex
                        val bg = if (ui.revealed && isCorrect)
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                        else
                            MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(bg)
                                .padding(12.dp)
                                .clickable(enabled = !ui.revealed) { /* optional: track selection */ }
                        ) {
                            Text(option)
                        }
                        Spacer(Modifier.height(8.dp))
                    }

                    Spacer(Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(onClick = { triviaCardViewModel.reveal() }, enabled = !ui.revealed) { Text("Reveal answer") }
                        Button(onClick = { triviaCardViewModel.loadNewQuestion(hobbies) }) { Text("New question") }
                    }
                }
            }
        }
    }
}