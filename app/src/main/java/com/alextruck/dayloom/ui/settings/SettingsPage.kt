package com.alextruck.dayloom.ui.settings

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.alextruck.dayloom.R
import com.alextruck.dayloom.util.LanguageManager

fun Context.findActivity(): Activity? {
    var context = this
    while (context is ContextWrapper) {
        if (context is Activity) return context
        context = context.baseContext
    }
    return null
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsPage(
    onNavigateBack: () -> Unit
) {
    val context = LocalContext.current
    var notificationsEnabled by remember { mutableStateOf(true) }
    var quietHours by remember { mutableStateOf(true) }

    val currentLocale = AppCompatDelegate.getApplicationLocales().toLanguageTags()
    var selectedLanguage by remember { mutableStateOf(if (currentLocale.startsWith("de")) "de" else "en") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.settings)) },
                navigationIcon = {
                    TextButton(onClick = onNavigateBack) {
                        Text(stringResource(R.string.back))
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
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Language Selection Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = stringResource(R.string.language), style = MaterialTheme.typography.titleLarge)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Button(
                            onClick = {
                                selectedLanguage = "en"
                                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("en"))
                                LanguageManager.saveLastRoute(context, "settings")
                                Handler(Looper.getMainLooper()).postDelayed({
                                    context.findActivity()?.recreate()
                                }, 100)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedLanguage == "en") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(stringResource(R.string.english))
                        }
                        Button(
                            onClick = {
                                selectedLanguage = "de"
                                AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("de"))
                                LanguageManager.saveLastRoute(context, "settings")
                                Handler(Looper.getMainLooper()).postDelayed({
                                    context.findActivity()?.recreate()
                                }, 100)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (selectedLanguage == "de") MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(stringResource(R.string.deutsch))
                        }
                    }
                }
            }

            // Notifications Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = stringResource(R.string.notifications), style = MaterialTheme.typography.titleLarge)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.daily_gentle_prompts))
                        Switch(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(R.string.quiet_hours))
                        Switch(checked = quietHours, onCheckedChange = { quietHours = it })
                    }
                }
            }

            // Data Archive Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text(text = stringResource(R.string.data_archive), style = MaterialTheme.typography.titleLarge)
                    Button(
                        onClick = { /* Export JSON */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = stringResource(R.string.export_data))
                    }
                    OutlinedButton(
                        onClick = { /* Import JSON */ },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(text = stringResource(R.string.import_data))
                    }
                }
            }

            Text(
                text = stringResource(R.string.app_footer),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}
