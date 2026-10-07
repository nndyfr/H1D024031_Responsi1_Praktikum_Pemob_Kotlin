package com.pemob.resepnindya.ui.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.pemob.resepnindya.ui.screens.DetailScreen
import com.pemob.resepnindya.ui.screens.HomeScreen
import com.pemob.resepnindya.ui.viewmodel.RecipeViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val recipeViewModel: RecipeViewModel = viewModel()

    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(
                viewModel = recipeViewModel,
                navigateToDetail = { idMeal ->
                    navController.navigate("detail/$idMeal")
                }
            )
        }
        composable(
            route = "detail/{idMeal}",
            arguments = listOf(navArgument("idMeal") { type = NavType.StringType })
        ) { backStackEntry ->
            val idMeal = backStackEntry.arguments?.getString("idMeal") ?: return@composable
            DetailScreen(
                idMeal = idMeal,
                viewModel = recipeViewModel,
                navigateBack = { navController.navigateUp() }
            )
        }
    }
}
