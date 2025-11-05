package uk.ac.tees.mad.bloodbond.ui.navigaion

import android.R
import kotlinx.serialization.Serializable


sealed class Routes {
    @Serializable
    data object AuthScreen

    @Serializable
    data class SignInScreen(val title:String)

    @Serializable
    data class LogInScreen(val title:String)
    @Serializable
    data object HomeScreen

}