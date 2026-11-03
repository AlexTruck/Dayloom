package com.alextruck.dayloom.ui.navigation

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.alextruck.dayloom.ui.create.ThreadCreationPage
import com.alextruck.dayloom.ui.home.HomePage
import com.alextruck.dayloom.ui.home.HomeViewModel
import com.alextruck.dayloom.ui.settings.SettingsPage
import com.alextruck.dayloom.ui.weave.WeaveDetailPage
import com.alextruck.dayloom.domain.model.Weave
import com.alextruck.dayloom.ui.echo.EchoPage
import com.alextruck.dayloom.util.LanguageManager
import java.util.Locale

@Composable
fun DayloomNavGraph() {
    val context = LocalContext.current
    val startDestination = remember { LanguageManager.consumeLastRoute(context) }
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()

    val currentLocale = AppCompatDelegate.getApplicationLocales().toLanguageTags()
    val isGerman = currentLocale.startsWith("de") || Locale.getDefault().language.equals("de", ignoreCase = true)

    val defaultPrompt = if (isGerman) "Was beschäftigt dich gerade?" else "What is on your mind?"

    LaunchedEffect(startDestination) {
        if (startDestination != "home") {
            navController.navigate(startDestination) {
                launchSingleTop = true
            }
        }
    }

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomePage(
                viewModel = homeViewModel,
                onNavigateToCreate = { prompt ->
                    navController.navigate("create?prompt=$prompt")
                },
                onNavigateToWeave = { date ->
                    navController.navigate("weave/$date")
                },
                onNavigateToEcho = { threadId ->
                    navController.navigate("echo/$threadId")
                },
                onNavigateToSettings = {
                    navController.navigate("settings")
                }
            )
        }
        composable(
            route = "create?prompt={prompt}",
            arguments = listOf(navArgument("prompt") { defaultValue = defaultPrompt })
        ) { backStackEntry ->
            val prompt = backStackEntry.arguments?.getString("prompt") ?: defaultPrompt
            ThreadCreationPage(
                prompt = prompt,
                onSaveThread = { type, content, promptQuestion ->
                    homeViewModel.addThread(type, content, promptQuestion)
                    navController.popBackStack()
                },
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
        composable(
            route = "weave/{date}",
            arguments = listOf(navArgument("date") { type = NavType.StringType })
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val mood = if (isGerman) "Ein friedlicher Tag voller sanfter Momente." else "A peaceful day filled with gentle moments."
            val sampleWeave = Weave(
                dateString = date,
                moodSummary = mood,
                colorPaletteHex = listOf("#C86D51", "#7A8B79", "#F3EFEA"),
                keywords = listOf(if (isGerman) "ruhe" else "calm", if (isGerman) "fokus" else "focus", if (isGerman) "ruhe" else "rest"),
                createdAt = System.currentTimeMillis()
            )
            WeaveDetailPage(
                weave = sampleWeave,
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = "echo/{threadId}",
            arguments = listOf(navArgument("threadId") { type = NavType.LongType })
        ) { backStackEntry ->
            val threadId = backStackEntry.arguments?.getLong("threadId") ?: 0L
            val sampleThread = com.alextruck.dayloom.domain.model.Thread(
                id = threadId,
                timestamp = System.currentTimeMillis() - 30L * 24 * 60 * 60 * 1000L,
                dateString = if (isGerman) "Vor 30 Tagen" else "30 days ago",
                type = com.alextruck.dayloom.domain.model.ThreadType.WORD,
                content = if (isGerman) "Auf der Suche nach Klarheit" else "Seeking clarity",
                promptQuestion = if (isGerman) "Welches Wort beschreibt deine Morgenenergie?" else "What word describes your energy?"
            )
            EchoPage(
                thread = sampleThread,
                onSaveReflection = {
                    navController.popBackStack()
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }
        composable("settings") {
            SettingsPage(
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
