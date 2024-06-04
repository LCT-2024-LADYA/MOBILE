package ru.gozerov.presentation.activity

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.gozerov.domain.usecases.LoginAsTraineeUseCase
import ru.gozerov.presentation.navigation.NavHostContainer
import ru.gozerov.presentation.shared.utils.RequestNotifications
import ru.gozerov.presentation.ui.theme.FitLadyaTheme
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var loginAsTraineeUseCase: LoginAsTraineeUseCase

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        /* val exercise = Exercise(
             id = 1,
             photoUrl = "https://s3-alpha-sig.figma.com/img/2e34/f886/b5a779583ee84526f6e6056df16f49d5?Expires=1718582400&Key-Pair-Id=APKAQ4GOSFWCVNEHN3O4&Signature=lBg3M3B5rrJNdzQk0878VyrEb-6tE6mXhAEaJgpBq96WTa31sbg7zVXdt3nNLe51u4BtY-lPHaJQCic~ALImf-kkNRY8q9rbQW3yQ23otrKjT0xTu26tmCyVambij6EhJrPzR~4VV4JyGS5~1B78PeQmzF~LupXVUBke-sNdHCD64Szshbqxc3bKe17PKaON4e6pgukBjj9gfZ0Xe8gF8AbmPsAHdlzwk742makolE2GiMoDdM9-gu8~SLqqTAlmJAqEBDVtfUYtf4NDU0OyLe~b0xCGrJlP6rbJzXtarAAzLDG4hCSDP3hnc8pOV7HRTouc3UUwSRUdvha4Trx61w__",
             name = "Жим штанги лежа",
             tags = listOf("Начинающий", "Базовое", "Грудь", "Штанга"),
             weight = 50.0,
             setsCount = 4,
             repsCount = 8
         )*/
        WindowCompat.setDecorFitsSystemWindows(window, true)
        setContent {
            val navController = rememberNavController()
            RequestNotifications()

            FitLadyaTheme {
                enableEdgeToEdge()
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    Scaffold(
                        containerColor = FitLadyaTheme.colors.primaryBackground
                    ) { paddingValues ->
                        NavHostContainer(
                            navController = navController,
                            padding = paddingValues,
                            true
                        )
                    }
                }
            }
        }
    }

}