package com.alextruck.dayloom.ui.create

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alextruck.dayloom.domain.model.ThreadType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThreadCreationPage(
    prompt: String,
    onSaveThread: (ThreadType, String, String) -> Unit,
    onNavigateBack: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) } // 0: Emoji, 1: Word, 2: Tag
    var textInput by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("✨") }

    val emojis = listOf("✨", "🌿", "☕", "🌊", "🌙", "🍂", "☀️", "🕊️", "🌧️", "🔥", "🌸", "💡")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weave a Thread") },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text("Cancel")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            // Prompt Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Today's Prompt",
                        style = MaterialTheme.typography.labelLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = prompt,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }

            // Tab Selector
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }, text = { Text("Emoji") })
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }, text = { Text("One Word") })
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }, text = { Text("Tag") })
            }

            // Input Content based on tab
            Box(modifier = Modifier.weight(1f)) {
                when (selectedTab) {
                    0 -> {
                        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            Text(text = "Selected: $selectedEmoji", style = MaterialTheme.typography.headlineMedium)
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(4),
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                items(emojis) { emoji ->
                                    Box(
                                        modifier = Modifier
                                            .size(64.dp)
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(if (selectedEmoji == emoji) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.surface)
                                            .clickable { selectedEmoji = emoji },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(text = emoji, fontSize = 28.sp)
                                    }
                                }
                            }
                        }
                    }
                    1 -> {
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            label = { Text("Enter one word...") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    2 -> {
                        OutlinedTextField(
                            value = textInput,
                            onValueChange = { textInput = it },
                            label = { Text("Enter tag (e.g. #calm)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }
            }

            // Save Button
            Button(
                onClick = {
                    when (selectedTab) {
                        0 -> onSaveThread(ThreadType.EMOJI, selectedEmoji, prompt)
                        1 -> if (textInput.isNotBlank()) onSaveThread(ThreadType.WORD, textInput.trim(), prompt)
                        2 -> {
                            val tag = if (textInput.startsWith("#")) textInput.trim() else "#${textInput.trim()}"
                            if (textInput.isNotBlank()) onSaveThread(ThreadType.TAG, tag, prompt)
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Text(text = "Weave Thread", fontSize = 16.sp)
            }
        }
    }
}
