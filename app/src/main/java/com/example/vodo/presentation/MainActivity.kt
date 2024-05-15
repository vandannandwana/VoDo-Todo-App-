package com.example.vodo.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.vodo.presentation.navigation.Navigation
import com.example.vodo.presentation.viewmodel.TaskViewModel
import com.example.vodo.ui.theme.VoDoTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            VoDoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    val viewModel = hiltViewModel<TaskViewModel>()
                    Navigation(this,viewModel)
                }
            }
        }
    }
}