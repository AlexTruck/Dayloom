package com.alextruck.dayloom.ui.echo

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.alextruck.dayloom.domain.model.Thread
import com.alextruck.dayloom.ui.theme.LocalStrings

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EchoPage(
    thread: Thread?,
    onSaveReflection: (String) -> Unit,
    onNavigateBack: () -> Unit
) {
    val strings = LocalStrings.current
    var reflectionText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(strings.echoTitle) },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text(strings.back)
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
            if (thread == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = androidx.compose.ui.Alignment.Center) {
                    Text(strings.threadNotFound)
                }
            } else {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(modifier = Modifier.padding(24.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text(
                            text = "${strings.echoFromPast30} (${thread.dateString})",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                        Text(
                            text = "“${thread.content}”",
                            style = MaterialTheme.typography.headlineMedium
                        )
                        Text(
                            text = "${strings.todaysPrompt}: ${thread.promptQuestion}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                        )
                    }
                }

                OutlinedTextField(
                    value = reflectionText,
                    onValueChange = { reflectionText = it },
                    label = { Text(strings.reflectionPrompt) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    shape = RoundedCornerShape(16.dp)
                )

                Button(
                    onClick = {
                        if (reflectionText.isNotBlank()) {
                            onSaveReflection(reflectionText.trim())
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Text(text = strings.saveReflection, fontSize = 16.sp)
                }
            }
        }
    }
}
