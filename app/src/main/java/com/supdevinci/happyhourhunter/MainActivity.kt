package com.supdevinci.happyhourhunter

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.supdevinci.happyhourhunter.ui.theme.HappyHourHunterTheme
import com.supdevinci.happyhourhunter.view.CocktailDetailScreen
import com.supdevinci.happyhourhunter.view.MainScreen
import com.supdevinci.happyhourhunter.viewmodel.CocktailDetailViewModel
import com.supdevinci.happyhourhunter.viewmodel.CocktailSearchViewModel
import com.supdevinci.happyhourhunter.viewmodel.WeatherCocktailViewModel
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val weatherViewModel: WeatherCocktailViewModel by viewModels()
    private val detailViewModel: CocktailDetailViewModel by viewModels()
    private val searchViewModel: CocktailSearchViewModel by viewModels()

    private val fusedLocationClient by lazy { LocationServices.getFusedLocationProviderClient(this) }

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
            var selectedCocktailId by remember { mutableStateOf<String?>(null) }

            BackHandler(enabled = selectedCocktailId != null) {
                selectedCocktailId = null
            }
            HappyHourHunterTheme {
                if (selectedCocktailId == null) {
                    MainScreen(
                        weatherViewModel = weatherViewModel,
                        searchViewModel = searchViewModel,
                        onCocktailClick = { id ->
                            selectedCocktailId = id
                            detailViewModel.fetchCocktailDetail(id)
                        }
                    )
                } else {
                    CocktailDetailScreen(
                        viewModel = detailViewModel,
                        onBack = { selectedCocktailId = null }
                    )
                }
            }
        }
    }

    private fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(
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