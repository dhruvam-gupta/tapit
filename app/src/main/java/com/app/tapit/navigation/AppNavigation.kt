package com.app.tapit.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.app.tapit.data.CategoryData
import com.app.tapit.ui.CategoryScreen
import com.app.tapit.ui.ColorScreen
import com.app.tapit.ui.WordScreen

object Routes {
    const val CATEGORIES = "categories"
    const val WORDS = "words/{categoryKey}"

    fun wordsRoute(categoryKey: String) = "words/$categoryKey"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.CATEGORIES
    ) {
        // Screen 1: Category grid
        composable(Routes.CATEGORIES) {
            CategoryScreen(
                onCategoryClick = { category ->
                    navController.navigate(Routes.wordsRoute(category.key))
                }
            )
        }

        // Screen 2: Word / Color learning screen
        composable(
            route = Routes.WORDS,
            arguments = listOf(
                navArgument("categoryKey") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val categoryKey = backStackEntry.arguments?.getString("categoryKey") ?: return@composable
            val category = CategoryData.getCategoryByKey(categoryKey) ?: return@composable

            if (category.key == "colors") {
                ColorScreen(
                    category = category,
                    onBackClick = { navController.popBackStack() }
                )
            } else {
                WordScreen(
                    category = category,
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}
