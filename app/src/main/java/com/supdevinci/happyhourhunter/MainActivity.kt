package com.supdevinci.happyhourhunter

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.core.content.ContextCompat
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.supdevinci.happyhourhunter.ui.theme.HappyHourHunterTheme
import com.supdevinci.happyhourhunter.view.navigation.CocktailNavHost
import com.supdevinci.happyhourhunter.view.navigation.Routes
import com.supdevinci.happyhourhunter.viewmodel.CocktailDetailViewModel
import com.supdevinci.happyhourhunter.viewmodel.CocktailSearchViewModel
import com.supdevinci.happyhourhunter.viewmodel.WeatherCocktailViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherCocktailViewModel by viewModels()
    private val detailViewModel: CocktailDetailViewModel by viewModels()
    private val searchViewModel: CocktailSearchViewModel by viewModels()

    private val fusedLocationClient by lazy {
        LocationServices.getFusedLocationProviderClient(this)
    }

    private val requestPermission = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) getCurrentLocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        checkLocationPermission()

        setContent {
            val navController = rememberNavController()
            val backStackEntry by navController.currentBackStackEntryAsState()
            val currentRoute = backStackEntry?.destination?.route

            HappyHourHunterTheme {
                Scaffold(
                    bottomBar = {
                        NavigationBar {
                            NavigationBarItem(
                                selected = currentRoute == Routes.HOME,
                                onClick = { navController.navigate(Routes.HOME) },
                                icon = { Icon(Icons.Outlined.Home, contentDescription = null) },
                                label = { Text("Accueil") }
                            )

                            NavigationBarItem(
                                selected = currentRoute == Routes.SEARCH,
                                onClick = { navController.navigate(Routes.SEARCH) },
                                icon = { Icon(Icons.Outlined.Search, contentDescription = null) },
                                label = { Text("Recherche") }
                            )

                            NavigationBarItem(
                                selected = currentRoute == Routes.FAVORITES,
                                onClick = { navController.navigate(Routes.FAVORITES) },
                                icon = { Icon(Icons.Outlined.Favorite, contentDescription = null) },
                                label = { Text("Favoris") }
                            )
                        }
                    }
                ) { innerPadding ->
                    CocktailNavHost(
                        navController = navController,
                        weatherViewModel = weatherViewModel,
                        searchViewModel = searchViewModel,
                        detailViewModel = detailViewModel,
                        modifier = androidx.compose.ui.Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            getCurrentLocation()
        } else {
            requestPermission.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentLocation() {
        fusedLocationClient.getCurrentLocation(
            Priority.PRIORITY_HIGH_ACCURACY,
            null
        ).addOnSuccessListener { location ->
            location?.let {
                weatherViewModel.fetchCocktailsForLocation(
                    lat = it.latitude,
                    lon = it.longitude,
                    city = getCityName(it.latitude, it.longitude)
                )
            }
        }
    }

    private fun getCityName(lat: Double, lon: Double): String =
        try {
            val addresses = Geocoder(this, Locale.FRENCH).getFromLocation(lat, lon, 1)
            addresses?.firstOrNull()?.locality ?: "Ville inconnue"
        } catch (e: Exception) {
            "Ville inconnue"
        }
}
