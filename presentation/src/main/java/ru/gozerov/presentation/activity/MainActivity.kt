package ru.gozerov.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.gozerov.presentation.R
import ru.gozerov.presentation.activity.models.MainActivityEffect
import ru.gozerov.presentation.navigation.NavHostContainer
import ru.gozerov.presentation.navigation.Screen
import ru.gozerov.presentation.shared.utils.RequestNotifications
import ru.gozerov.presentation.ui.theme.FitLadyaTheme


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val startDestination = remember { mutableStateOf<String?>(null) }

            LaunchedEffect(null) {
                viewModel.effect.collect { effect ->
                    when (effect) {
                        is MainActivityEffect.None -> {}
                        is MainActivityEffect.AuthResult -> {
                            startDestination.value =
                                if (effect.navigateToLogin) Screen.ChoiceLogin.route else {
                                    if (effect.isClient) Screen.TraineeTabs.route else Screen.TrainerProfile.route
                                }
                        }

                        is MainActivityEffect.Error -> {
                            Toast.makeText(
                                this@MainActivity,
                                getString(R.string.error),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
            val navController = rememberNavController()
            RequestNotifications()

            FitLadyaTheme {
                val barColor = FitLadyaTheme.colors.primaryBackground.toArgb()
                SideEffect {
                    window.statusBarColor = barColor
                    window.navigationBarColor = barColor
                }
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Scaffold(
                        containerColor = FitLadyaTheme.colors.primaryBackground
                    ) { paddingValues ->

                        startDestination.value?.let { destination ->
                            NavHostContainer(
                                startDestination = destination,
                                navController = navController,
                                padding = paddingValues
                            )
                        }
                    }
                }
            }
        }
    }

}