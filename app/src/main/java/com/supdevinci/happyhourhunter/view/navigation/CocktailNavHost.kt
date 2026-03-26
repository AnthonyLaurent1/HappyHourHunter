package com.supdevinci.happyhourhunter.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavType
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.supdevinci.happyhourhunter.view.CocktailDetailScreen
import com.supdevinci.happyhourhunter.view.FavoritesScreen
import com.supdevinci.happyhourhunter.view.MainScreen
import com.supdevinci.happyhourhunter.view.SearchScreen
import com.supdevinci.happyhourhunter.view.SplashScreen
import com.supdevinci.happyhourhunter.viewmodel.CocktailDetailViewModel
import com.supdevinci.happyhourhunter.viewmodel.CocktailSearchViewModel
import com.supdevinci.happyhourhunter.viewmodel.FavoritesViewModel
import com.supdevinci.happyhourhunter.viewmodel.WeatherCocktailViewModel

object Routes {
    const val HOME = "home"
    const val SEARCH = "search"
    const val DETAIL = "detail"
    const val FAVORITES = "favorites"
    const val SPLASH = "splash"

}

@Composable
fun CocktailNavHost(
    navController: NavHostController,
    weatherViewModel: WeatherCocktailViewModel,
    searchViewModel: CocktailSearchViewModel,
    detailViewModel: CocktailDetailViewModel,
    favoritesViewModel: FavoritesViewModel,
    modifier: Modifier = Modifier
) {
    val isInitialLoading by weatherViewModel.isInitialLoading.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = Routes.SPLASH,
        modifier = modifier
    ) {
        composable(Routes.SPLASH) {
            LaunchedEffect(isInitialLoading) {
                if (!isInitialLoading) {
                    navController.navigate(Routes.HOME) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                }
            }
            SplashScreen()
        }
        composable(Routes.HOME) {
            MainScreen(
                weatherViewModel = weatherViewModel,
                onCocktailClick = { id ->
                    navController.navigate("${Routes.DETAIL}/$id")
                }
            )
        }

        composable(Routes.SEARCH) {
            SearchScreen(
                searchViewModel = searchViewModel,
                onCocktailClick = { id ->
                    navController.navigate("${Routes.DETAIL}/$id")
                }
            )
        }

        composable(Routes.FAVORITES) {
            FavoritesScreen(
                favoritesViewModel = favoritesViewModel,
                onCocktailClick = { id ->
                    navController.navigate("${Routes.DETAIL}/$id")
                }
            )
        }

        composable(
            route = "${Routes.DETAIL}/{cocktailId}",
            arguments = listOf(
                navArgument("cocktailId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("cocktailId") ?: ""

            LaunchedEffect(id) {
                detailViewModel.fetchCocktailDetail(id)
            }

            CocktailDetailScreen(
                viewModel = detailViewModel,
                onBack = { navController.popBackStack() }
            )
        }

    }
}
