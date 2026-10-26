package com.alextruck.dayloom

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.alextruck.dayloom.ui.navigation.DayloomNavGraph
import com.alextruck.dayloom.ui.theme.DayloomTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DayloomTheme {
                DayloomNavGraph()
            }
        }
    }
}
