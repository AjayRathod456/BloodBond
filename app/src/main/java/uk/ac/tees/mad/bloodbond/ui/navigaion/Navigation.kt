package uk.ac.tees.mad.bloodbond.ui.navigaion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.AuthScreen
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.LoginScreen
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.SignUpScreen


@Composable
fun Navigation( modifier : Modifier = Modifier) {


    val navController: NavHostController = rememberNavController()
    val firstScreen = if (FirebaseAuth.getInstance().currentUser?.uid != null) Routes.AuthScreen else Routes.HomeScreen


    NavHost(navController = navController, startDestination = Routes.AuthScreen) {

        composable<Routes.AuthScreen> {

            AuthScreen(

                navController =navController
            )

        }

        composable<Routes.SignInScreen> {
            val args = it.toRoute<Routes.SignInScreen>()
            SignUpScreen(
                title = args.title,
                navController = navController
            )


        }

        composable<Routes.LogInScreen> {


            val args = it.toRoute<Routes.LogInScreen>()
            LoginScreen(
                title =args.title,
                navController =navController
            )


        }


    }
}