package uk.ac.tees.mad.bloodbond

import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import uk.ac.tees.mad.bloodbond.ui.navigaion.Navigation
import uk.ac.tees.mad.bloodbond.ui.screens.AuthViewModel
import uk.ac.tees.mad.bloodbond.ui.theme.BloodbondTheme


@AndroidEntryPoint
class MainActivity : ComponentActivity() {


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)

        val settings = firestoreSettings {
            isPersistenceEnabled = true
            cacheSizeBytes = FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED
        }

        FirebaseFirestore.getInstance().firestoreSettings = settings


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                101
            )
        }

        scheduleDailyNotifications(this)


        installSplashScreen()
        enableEdgeToEdge()

        setContent {
            val authViewModel: AuthViewModel = hiltViewModel()

            BloodbondTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                    Navigation(
                        modifier = Modifier.padding(innerPadding),
                        authViewModel = authViewModel
                    )
                      authViewModel.fetchDonorFromFirestore()
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