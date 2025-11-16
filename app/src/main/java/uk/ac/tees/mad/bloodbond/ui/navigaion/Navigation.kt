package uk.ac.tees.mad.bloodbond.ui.navigaion



import DonorDetailScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.AuthScreen
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.AuthViewModel
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.DonerRegistrationScreen
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.LoginScreen
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.ReceiverSignScreen
import uk.ac.tees.mad.bloodbond.ui.screens.otherScreens.HomeScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel) {


    val navController: NavHostController = rememberNavController()

    val auth = FirebaseAuth.getInstance()


    var currentUser by remember { mutableStateOf(auth.currentUser) }

    DisposableEffect(Unit) {
        val listener = FirebaseAuth.AuthStateListener {
            currentUser = it.currentUser
        }
        auth.addAuthStateListener(listener)
        onDispose { auth.removeAuthStateListener(listener) }
    }

    val startDestination = if (currentUser == null) {
        Routes.AuthScreen
    } else {
        Routes.HomeScreen
    }


    NavHost(navController = navController, startDestination = startDestination ) {

        composable<Routes.AuthScreen> {

            AuthScreen(

                navController = navController
            )

        }

        composable<Routes.DonerRegistrationScreen> {
            val args = it.toRoute<Routes.DonerRegistrationScreen>()

            DonerRegistrationScreen(
                title = args.title,
                navController = navController,
                authViewModel = authViewModel
            )


        }

        composable<Routes.ReceiverSignScreen> {

            val args = it.toRoute<Routes.ReceiverSignScreen>()


            ReceiverSignScreen(
                navController = navController,
                title = args.title,
                viewModel = authViewModel
            )


        }

        composable<Routes.LogInScreen> {


            val args = it.toRoute<Routes.LogInScreen>()

            LoginScreen(
                title = args.title,
                navController = navController,
                viewModel = authViewModel
            )


        }



        composable<Routes.HomeScreen> {




            HomeScreen(

                authViewModel = authViewModel,
                navController = navController,

            )

        }

        composable<Routes.DonorDetail> {

            val args = it.toRoute<Routes.DonorDetail>()

            DonorDetailScreen(
                viewModel = authViewModel,
                name = args.name,
                mobile = args.mobile,
                bloodGroup = args.bloodGroup,
                date = args.date,
                imageUrl = args.imageUrl,
                uid = args.uid,

            )

        }






    }
}