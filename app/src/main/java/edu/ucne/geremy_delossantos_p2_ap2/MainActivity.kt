package edu.ucne.geremy_delossantos_p2_ap2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import edu.ucne.geremy_delossantos_p2_ap2.presentation.navegation.ApiNavHost
import edu.ucne.geremy_delossantos_p2_ap2.presentation.sistema.ApiViewModel
import edu.ucne.geremy_delossantos_p2_ap2.ui.theme.Geremy_delosSantos_P2_AP2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Geremy_delosSantos_P2_AP2Theme {
                val navController = rememberNavController()
                val ApiViewModel: ApiViewModel = hiltViewModel()


                ApiNavHost(
                    navHostController = navController,
                    apiViewModel = ApiViewModel,

                    )
            }
        }
    }
}


@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Geremy_delosSantos_P2_AP2Theme {
        Greeting("Android")
    }
}