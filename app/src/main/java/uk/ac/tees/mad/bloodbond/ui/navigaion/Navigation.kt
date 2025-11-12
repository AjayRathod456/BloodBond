package uk.ac.tees.mad.bloodbond.ui.navigaion

import ProfileScreen
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute

import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.AuthScreen
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.AuthViewModel
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.LoginScreen
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.DonerRegistrationScreen
import uk.ac.tees.mad.bloodbond.ui.screens.authScreen.ReceiverSignScreen



@Composable
fun Navigation( modifier : Modifier = Modifier,authViewModel: AuthViewModel) {


    val navController: NavHostController = rememberNavController()
    val firstScreen = if (FirebaseAuth.getInstance().currentUser?.uid != null) Routes.Profile else Routes.AuthScreen


    NavHost(navController = navController, startDestination =  firstScreen ) {

        composable<Routes.AuthScreen> {

            AuthScreen(

                navController =navController
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
            val args = it.toRoute<Routes.DonerRegistrationScreen>()
            ReceiverSignScreen(
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

        composable<Routes.Profile> {


            ProfileScreen(viewModel  = authViewModel)

        }



    }
}