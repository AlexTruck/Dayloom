package com.alextruck.dayloom

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.alextruck.dayloom.ui.navigation.DayloomNavGraph
import com.alextruck.dayloom.ui.theme.DayloomTheme

class MainActivity : AppCompatActivity() {
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
