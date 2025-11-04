package uk.ac.tees.mad.bloodbond.ui.navigaion

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.bloodbond.Greeting


@Composable
fun Navigation(modifier: Modifier = Modifier) {



    val navController: NavHostController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.SplashScreen.name){


        composable(Routes.SplashScreen.name) {



        }

     }
}