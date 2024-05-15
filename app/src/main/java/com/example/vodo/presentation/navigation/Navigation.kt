package com.example.vodo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.vodo.presentation.components.main_screen.HomeScreen
import com.example.vodo.presentation.components.splash_screen.SplashScreen
import com.example.vodo.presentation.viewmodel.TaskViewModel

@Composable
fun Navigation(lifecycleOwner: LifecycleOwner, viewModel: TaskViewModel) {

    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "splash") {

        composable("splash") {
            SplashScreen(onNavigate = {navController.popBackStack();navController.navigate("home")})
        }

        composable("home") {
            HomeScreen(lifecycleOwner = lifecycleOwner, viewModel = viewModel)
        }
    }
}