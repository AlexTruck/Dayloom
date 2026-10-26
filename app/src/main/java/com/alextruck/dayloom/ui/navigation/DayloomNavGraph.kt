package com.alextruck.dayloom.ui.navigation

import androidx.compose.runtime.Composable
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

@Composable
fun DayloomNavGraph() {
    val navController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()

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
            arguments = listOf(navArgument("prompt") { defaultValue = "What is on your mind?" })
        ) { backStackEntry ->
            val prompt = backStackEntry.arguments?.getString("prompt") ?: "What is on your mind?"
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
            // Simple mock/sample weave retrieval or from VM
            val sampleWeave = Weave(
                dateString = date,
                moodSummary = "A peaceful day filled with gentle moments.",
                colorPaletteHex = listOf("#C86D51", "#7A8B79", "#F3EFEA"),
                keywords = listOf("calm", "focus", "rest"),
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
                dateString = "30 days ago",
                type = com.alextruck.dayloom.domain.model.ThreadType.WORD,
                content = "Seeking clarity",
                promptQuestion = "What word describes your energy?"
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
