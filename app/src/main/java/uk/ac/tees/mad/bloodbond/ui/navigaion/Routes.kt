package uk.ac.tees.mad.bloodbond.ui.navigaion

import kotlinx.serialization.Serializable

sealed class Routes(val name: String) {

    data object SplashScreen: Routes("splash_screen")


}