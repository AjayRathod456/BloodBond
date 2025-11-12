package uk.ac.tees.mad.bloodbond

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import uk.ac.tees.mad.bloodbond.ui.navigaion.Navigation
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.AuthViewModel
import uk.ac.tees.mad.bloodbond.ui.theme.BloodbondTheme

class MainActivity : ComponentActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        setContent {

            val authViewModel: AuthViewModel = viewModel()
            BloodbondTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Navigation(modifier = Modifier.padding(innerPadding),
                    authViewModel = authViewModel)


                }
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
    BloodbondTheme {
        Greeting("Android")
    }
}